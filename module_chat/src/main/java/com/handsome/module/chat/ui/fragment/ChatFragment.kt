package com.handsome.module.chat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.MAIN_CHAT
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.service.impl
import com.handsome.module.chat.databinding.ChatFragmentChatBinding
import com.handsome.module.chat.ui.activity.ContentListActivity
import com.handsome.module.chat.ui.adapter.ChatListAdapter
import com.handsome.module.chat.ui.viewmodel.ChatViewModel

@Route(path = MAIN_CHAT)
class ChatFragment : BaseFragment() {
    private val mBinding by lazy { ChatFragmentChatBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<ChatViewModel>()
    private val mUserInfo by lazy { IAccountService::class.impl.getUserInfo() }
    private lateinit var mAdapter: ChatListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mUserInfo == null) {
            toast("用户异常")
            return
        }
        initRv()
        initObserve()
    }

    override fun onStart() {
        super.onStart()
        initData()
    }

    private fun initData() {
        mViewModel.getChatList(mUserInfo!!.user_id)
    }

    private fun initObserve() {
        mViewModel.chatList.collectLaunch {
            if (it != null) {
                if (it.status_code == 0) {
                    if (it.user_list != null){
                        // 这里因为远端没有设置字段，所以其实也就和submitList结果一样
                        mAdapter.submitListToOrder(it.user_list)
                    }
                } else {
                    toast("消息请求失败")
                }
            }
        }
    }

    private fun initRv() {
        mAdapter = ChatListAdapter(mUserInfo!!.user_id).apply {
            setOnClickChatItem { selfId, otherId ->
                ContentListActivity.startAction(requireContext(),selfId,otherId)
            }
            setOnClickDelete {
                deleteItem(it)
            }
            setOnClickTop { data,tv ->
                // 等到有接口的时候放到远端返回正确信息后再返回
                addItemToOrder(data,tv)
            }
            setOnItemSlideBack { curPosition ->
                val list = currentList.toMutableList()
                rightSlideOpenLoc?.let { lastPosition ->
                    list[curPosition].isOpen = true
                    list[lastPosition].isOpen = false
                    submitList(list)
                    notifyItemChanged(lastPosition)
                }
            }
        }
        with(mBinding.chatFragRv) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    // 正在拖动
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING){
                        with(mAdapter){
                            rightSlideOpenLoc?.let {
                                val list = currentList.toMutableList()
                                list[it].isOpen = false
                                submitList(list)
                                notifyItemChanged(it)
                                rightSlideOpenLoc = null
                            }
                        }
                    }
                }
            })
        }

    }
}