package com.handsome.module.chat.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.toast
import com.handsome.module.chat.databinding.ChatActivityContentListBinding
import com.handsome.module.chat.ui.adapter.ContentListAdapter
import com.handsome.module.chat.ui.viewmodel.ContentListViewModel

class ContentListActivity : BaseActivity() {
    private val mBinding by lazy { ChatActivityContentListBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<ContentListViewModel>()
    private lateinit var mAdapter: ContentListAdapter
    private val selfId: Long by lazy { intent.getLongExtra("selfId", -1) }
    private val otherId: Long by lazy { intent.getLongExtra("otherId", -1) }
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var lastMessageTime: Long = 0   //最后一条消息的时间，其实也可以通过adapter来获得

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        if (selfId == (-1).toLong() || otherId == (-1).toLong()) {
            toast("用户信息错误")
            return
        }
        initClick()
        initRv()
        initObserve()
        postSingle(lastMessageTime)
    }

    private fun initClick() {
        with(mBinding) {
            chatContentListImgBack.setOnClickListener {
                finishAfterTransition()
            }
            chatContentListTvSend.setOnClickListener {
                sendMessage()
            }
        }
    }

    private fun sendMessage() {
        val message = mBinding.chatContentListEtSend.text.toString().trim()
        if (message == "") {
            toast("输入不能为空")
            return
        }
        mViewModel.uploadMessage(otherId, message)
    }

    private fun getData(lastMessageTime: Long = 0) {
        mViewModel.getContentList(otherId, lastMessageTime)
    }

    private fun initObserve() {
        // 由于stateflow在结果相等的时候不会观察到，所以我们利用这点，直接handler一直请求。
        mViewModel.contentList.collectLaunch {
            Log.d("lx", "initObserve start:${it} ")
            if (it != null) {
                if (it.status_code == 0) {
                    val list = mAdapter.currentList.toMutableList()
                    if (it.message_list != null) {
                        list.addAll(it.message_list)
                        mAdapter.submitList(list)
                        lastMessageTime = list[list.size - 1].create_time
                        mBinding.chatActivityContentListRv.post {
                            mBinding.chatActivityContentListRv.smoothScrollToPosition(mBinding.chatActivityContentListRv.computeVerticalScrollRange())
                        }
                    }
                } else {
                    toast("获取消息列表失败！")
                }
                Log.d("lx", "initObserve end:${it} ")
            }
        }
        mViewModel.uploadMessage.collectLaunch {
            if (it != null) {
                if (it.status_code == 0) {
                    toast("发送成功~")
                } else {
                    toast("获取消息列表失败！")
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mRunnable?.let { mHandler?.removeCallbacks(it) }
    }

    /**
     * 一秒钟之后再次请求一次消息
     */
    private fun postSingle(lastMessageTime: Long, delayTime: Long = 1000) {
        if (mHandler == null) {
            mHandler = Handler(Looper.getMainLooper())
        }
        // 每次重新赋值，因为不能让runnable一样
        mRunnable = Runnable {
            Log.d("lx", "postSingle: ")
            postSingle(this.lastMessageTime)
            getData(lastMessageTime)
        }
        mHandler!!.postDelayed(mRunnable!!, delayTime)
    }

    private fun initRv() {
        mAdapter = ContentListAdapter(otherId to selfId)
        with(mBinding.chatActivityContentListRv) {
            layoutManager =
                LinearLayoutManager(this@ContentListActivity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
    }

    companion object {
        fun startAction(context: Context, selfId: Long, otherId: Long) {
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("selfId", selfId)
            intent.putExtra("otherId", otherId)
            context.startActivity(intent)
        }
    }

}