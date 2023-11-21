package com.handsome.module.main.ui.activity

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
import com.handsome.module.main.R
import com.handsome.module.main.databinding.MainActivityFollowListBinding
import com.handsome.module.main.ui.adapter.FollowAdapter
import com.handsome.module.main.ui.viewmodel.activity.FollowListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FollowListActivity : BaseActivity() {
    private val mBinding by lazy { MainActivityFollowListBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<FollowListViewModel>()
    private val mAdapter by lazy { FollowAdapter() }
    private lateinit var mCurrentUser : com.handsome.module.main.bean.AuthorBean   // 后面可以考虑替换成全局变量
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
                mViewModel.getFollowList(mCurrentUser.id)
            }
            FollowType.FANS -> {
                mViewModel.getFansList(mCurrentUser.id)
            }
        }
    }

    private fun initValue() {
        mCurrentUser = intent.getSerializableExtra("currentUser") as com.handsome.module.main.bean.AuthorBean
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
                        this@FollowListActivity,it)
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
                                it.setBackgroundResource(R.drawable.main_shape_follow_have_bg)
                            }else{
                                it.text = "关注"
                                it.setBackgroundResource(R.drawable.main_shape_follow_no_bg)
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
        fun startAction(context : Context, currentUser : com.handsome.module.main.bean.AuthorBean, followType: FollowType){
            val intent = Intent(context,FollowListActivity::class.java)
            intent.putExtra("currentUser",currentUser)
            intent.putExtra("followType",followType)
            context.startActivity(intent)
        }
    }
}