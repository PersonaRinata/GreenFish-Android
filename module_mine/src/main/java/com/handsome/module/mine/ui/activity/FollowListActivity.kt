package com.handsome.module.mine.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.gone
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.extention.visible
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
        mBinding.mineActivityFollowListImgBack.setOnClickListener {
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
        with(mBinding.mineActivityChatDetailRv) {
            layoutManager =
                LinearLayoutManager(this@FollowListActivity, RecyclerView.VERTICAL, false)
            adapter = mAdapter.apply {
                setOnClickFollow { user ->
                    mViewModel.followUser(user.id,if (user.is_follow) 2 else 1)
                    startLoad()
                }
                setOnClickUser {
                    PersonActivity.startAction(this@FollowListActivity,it.id)
                }
            }
        }
    }

    private fun startLoad(){
        mBinding.mineActivityProgressBar.visible()
        mBinding.root.isClickable = false
    }

    private fun stopLoad(){
        mBinding.mineActivityProgressBar.gone()
        mBinding.root.isClickable = true
    }


    private fun initObserve() {
        lifecycleScope.launch {
            mViewModel.followList.collectLatest {
                if (it != null) {
                    if (it.status_code == 0) {
                        stopLoad()
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
                        stopLoad()
                        mAdapter.submitList(it.user_list)
                    } else {
                        toast("网络异常")
                    }
                }
            }
        }

        lifecycleScope.launch {
            mViewModel.followUser.collectLatest {data ->
                if (data != null){
                    if (data.status_code == 0){
                        getData()  //刷新数据，用数据来说话
                    }else{
                        stopLoad()
                        toast("网络异常")
                    }
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