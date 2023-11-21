package com.handsome.module.main.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.main.databinding.MainFragmentChatBinding
import com.handsome.module.main.ui.activity.ChatDetailActivity
import com.handsome.module.main.ui.adapter.FriendsListAdapter
import com.handsome.module.main.ui.viewmodel.fragment.ChatFragmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatFragment : BaseFragment() {
    private val mBinding by lazy { MainFragmentChatBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<ChatFragmentViewModel>()
    private val mAdapter by lazy { FriendsListAdapter() }
    private var mUserInfo by arguments<com.handsome.module.main.bean.AuthorBean>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initRv()
        initObserve()
        getFriendsList()
        return mBinding.root
    }

    private fun initRv() {
        with(mBinding.mainFragmentChatRv){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter.apply {
                setOnClickItem {
                    ChatDetailActivity.startAction(requireContext(),mUserInfo,it)
                }
                setOnClickTop {
                    toast("置顶")
                }
                setOnClickDelete {
                    toast("删除")
                }
            }
        }
    }

    private fun getFriendsList() {
        mViewModel.getFriendsList()
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch(myCoroutineExceptionHandler){
            mViewModel.friendsList.collectLatest {
                if (it != null){
                    if (it.status_code == 0){
                        mAdapter.submitList(it.user_list)
                    }else{
                        toast("网络异常")
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(userInfo : com.handsome.module.main.bean.AuthorBean) = ChatFragment().apply {
            arguments = bundleOf(
                this::mUserInfo.name to userInfo
            )
        }
    }
}