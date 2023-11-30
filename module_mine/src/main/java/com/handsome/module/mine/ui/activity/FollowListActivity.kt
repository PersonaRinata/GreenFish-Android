package com.handsome.module.mine.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.toast
import com.handsome.module.mine.R
import com.handsome.module.mine.databinding.MineActivityFollowListBinding
import com.handsome.module.mine.ui.adapter.FollowAdapter
import com.handsome.module.mine.ui.viewmodel.FollowListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FollowListActivity : BaseActivity() {
    private val mBinding by lazy { MineActivityFollowListBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<FollowListViewModel>()
    private val mAdapter by lazy { FollowAdapter() }
    private var mUserId : Long = -1  // 后面可以考虑替换成全局变量
    private lateinit var mType : FollowType
    private var mTextView : TextView? = null
    private val isFollowList by lazy { HashMap<String,Boolean>() }  //同一个人的记录

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initValue()
        initObserve()
        initRv()
        initBack()
        getData()
    }

    private fun initBack() {
        mBinding.mainActivityFollowListImgBack.setOnClickListener {
            finish()
        }
    }

    private fun getData() {
        when(mType){
            FollowType.FOLLOWS -> {
                mViewModel.getFollowList(mUserId)
            }
            FollowType.FANS -> {
                mViewModel.getFansList(mUserId)
            }
        }
    }

    private fun initValue() {
        mUserId = intent.getSerializableExtra("userId") as Long
        mType = intent.getSerializableExtra("followType") as FollowType
    }

    private fun initRv() {
        with(mBinding.mainActivityChatDetailRv) {
            layoutManager =
                LinearLayoutManager(this@FollowListActivity, RecyclerView.VERTICAL, false)
            adapter = mAdapter.apply {
                setOnClickFollow { user, textView ->
                    mTextView = textView
                    // 第一次没初始化
                    if (!isFollowList.containsKey(user.id.toString())){
                        isFollowList[user.id.toString()] = user.is_follow
                    }
                    mViewModel.followUser(user.id,if (isFollowList[user.id.toString()] == true) 2 else 1)
                }
                setOnClickUser {
                    PersonActivity.startAction(
                        this@FollowListActivity,it.id)
                }
            }
        }
    }

    private fun initObserve() {
        lifecycleScope.launch {
            mViewModel.followList.collectLatest {
                if (it != null) {
                    if (it.status_code == 0) {
                        mAdapter.submitList(it.user_list)
                    } else {
                        toast("网络异常")
                    }
                }
            }
        }

        lifecycleScope.launch {
            mViewModel.fansList.collectLatest {
                if (it != null) {
                    if (it.status_code == 0) {
                        mAdapter.submitList(it.user_list)
                    } else {
                        toast("网络异常")
                    }
                }
            }
        }

        lifecycleScope.launch {
            mViewModel.followUser.collectLatest {data ->
                Log.d("lx", "videoFragment: follow $data ")
                if (data != null){
                    Log.d("lx", "videoFragment: isFollowList[it.first.toString()] ${isFollowList[data.first.toString()]} ")
                    if(data.second != null && data.second!!.status_code == 0){
                        mTextView?.let{
                            if (isFollowList[data.first.toString()]!!){
                                it.text = "已关注"
                                it.setBackgroundResource(R.drawable.mine_shape_follow_have_bg)
                            }else{
                                it.text = "关注"
                                it.setBackgroundResource(R.drawable.mine_shape_follow_no_bg)
                            }
                            isFollowList[data.first.toString()] = !isFollowList[data.first.toString()]!!
                        }
                    }else{
                        toast("网络异常")
                    }
                    mTextView = null
                }
            }
        }
    }

    enum class FollowType{
        FOLLOWS,FANS
    }

    companion object{
        fun startAction(context : Context, userId : Long, followType: FollowType){
            val intent = Intent(context,FollowListActivity::class.java)
            intent.putExtra("userId",userId)
            intent.putExtra("followType",followType)
            context.startActivity(intent)
        }
    }
}