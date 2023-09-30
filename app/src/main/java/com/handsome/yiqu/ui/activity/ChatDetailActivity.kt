package com.handsome.yiqu.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.toast
import com.handsome.yiqu.bean.AuthorBean
import com.handsome.yiqu.bean.ChatMessageBean
import com.handsome.yiqu.databinding.MainActivityChatDetailBinding
import com.handsome.yiqu.ui.adapter.ChatAdapter
import com.handsome.yiqu.ui.viewmodel.activity.ChatDetailActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatDetailActivity : BaseActivity() {
    private val mBinding by lazy { MainActivityChatDetailBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<ChatDetailActivityViewModel>()

    private lateinit var currentUser: AuthorBean
    private lateinit var otherUser: AuthorBean
    private lateinit var mAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initValue()
        initObserve()
        initBack()
        initRv()
        initSendMessage()
        getHistoryMessage()
    }

    private fun getHistoryMessage() {
        mViewModel.getMessageHistory(otherUser.id.toString())
    }

    private fun initSendMessage() {
        with(mBinding){
            mainDialogCommentTvSend.setOnClickListener {
                val content = mainActivityChatDetailEtSend.text.toString()
                mViewModel.chatMessage(otherUser.id.toString(),content)
                addContentToAdapter(content)
            }
        }
    }

    private fun addContentToAdapter(content : String){
        val list = mAdapter.currentList.toMutableList()
        val chatMessageBean = ChatMessageBean(0,otherUser.id,currentUser.id,content,System.currentTimeMillis())
        list.add(chatMessageBean)
        mAdapter.submitList(list)
    }

    private fun initObserve() {
        lifecycleScope.launch {
            mViewModel.chatMessage.collectLatest {
                if (it != null){
                    if (it.status_code == 0){
                        toast("发送成功")
                    }else{
                        toast("发送失败")
                    }
                }
            }
        }

        lifecycleScope.launch {
            mViewModel.messageHistory.collectLatest {
                if (it != null){
                    if (it.status_code == 0){
                        mAdapter.submitList(it.message_list)
                    }else{
                        toast("网络错误")
                    }
                }
            }
        }
    }

    private fun initBack() {
        with(mBinding.mainActivityChatDetailImgBack) {
            setOnClickListener {
                finish()
            }
        }
    }

    private fun initRv() {
        with(mBinding.mainActivityChatDetailRv) {
            layoutManager =
                LinearLayoutManager(this@ChatDetailActivity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
    }

    private fun initValue() {
        currentUser = intent.getSerializableExtra("currentUser") as AuthorBean
        otherUser = intent.getSerializableExtra("otherUser") as AuthorBean
        mAdapter = ChatAdapter(currentUser.id)
    }

    companion object {
        fun startAction(context: Context, currentUser: AuthorBean, otherUser: AuthorBean) {
            val intent = Intent(context, ChatDetailActivity::class.java)
            intent.putExtra("currentUser", currentUser)
            intent.putExtra("otherUser", otherUser)
            context.startActivity(intent)
        }
    }
}