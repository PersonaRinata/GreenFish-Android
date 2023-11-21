package com.handsome.module.main.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.toast
import com.handsome.module.main.databinding.MainActivityChatDetailBinding
import com.handsome.module.main.ui.adapter.ChatAdapter
import com.handsome.module.main.ui.viewmodel.activity.ChatDetailActivityViewModel
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatDetailActivity : BaseActivity() {
    private val mBinding by lazy { MainActivityChatDetailBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<ChatDetailActivityViewModel>()

    private lateinit var currentUser: com.handsome.module.main.bean.AuthorBean
    private lateinit var otherUser: com.handsome.module.main.bean.AuthorBean
    private lateinit var mAdapter: ChatAdapter

    private var mHandler : Handler? = null
    private var mRunnable : Runnable? = null

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

    override fun onStart() {
        super.onStart()
//        forMessage()
    }

    private fun forMessage() {
        mHandler = Handler(Looper.getMainLooper())
        mRunnable = Runnable {
            if (mAdapter.currentList.isNotEmpty()){
                getHistoryMessage(mAdapter.currentList[mAdapter.currentList.size - 1].create_time.toString())
            }
            mHandler!!.postDelayed(mRunnable!!,1000)
        }
        // 每秒查询一次
        mHandler!!.postDelayed(mRunnable!!,1000)
    }

    private fun getHistoryMessage(preMessageTime : String = "0") {
        mViewModel.getMessageHistory(otherUser.id.toString(),preMessageTime)
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
        val chatMessageBean = com.handsome.module.main.bean.ChatMessageBean(
            0,
            otherUser.id,
            currentUser.id,
            content,
            System.currentTimeMillis()
        )
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
//                        val list = mAdapter.currentList.toMutableList()
//                        it.message_list?.let { it1 -> list.addAll(it1) }
                        mAdapter.submitList(it.message_list)
                    }else{
                        toast("网络错误")
                    }
                }
            }
        }

        lifecycleScope.launch {
            mViewModel.userInfo.collectLatest {
                if (it != null){
                    if (it.status_code == 0){
                        PersonActivity.startAction(
                            this@ChatDetailActivity,
                            it.user)
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
            adapter = mAdapter.apply {
                setOnClickUser {
                    mViewModel.getUserInfo(it.to_user_id)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mRunnable?.let { mHandler?.removeCallbacks(it) }
    }

    private fun initValue() {
        currentUser = intent.getSerializableExtra("currentUser") as com.handsome.module.main.bean.AuthorBean
        otherUser = intent.getSerializableExtra("otherUser") as com.handsome.module.main.bean.AuthorBean
        mAdapter = ChatAdapter(currentUser.id)
    }

    companion object {
        fun startAction(context: Context, currentUser: com.handsome.module.main.bean.AuthorBean, otherUser: com.handsome.module.main.bean.AuthorBean) {
            val intent = Intent(context,ChatDetailActivity::class.java)
            intent.putExtra("currentUser", currentUser)
            intent.putExtra("otherUser", otherUser)
            context.startActivity(intent)
        }
    }
}