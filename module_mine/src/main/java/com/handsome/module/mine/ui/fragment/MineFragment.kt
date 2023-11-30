package com.handsome.module.mine.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.module.mine.databinding.MineFragmentMineBinding
import com.handsome.module.mine.ui.activity.FollowListActivity
import com.handsome.module.mine.ui.viewmodel.MineViewModel

// 后续考虑通过service
class MineFragment : BaseFragment() {
    private val mBinding by lazy { MineFragmentMineBinding.inflate(layoutInflater) }
    private val mUserId by arguments<Long>()
    private val mViewModel by viewModels<MineViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObserve()
        initView()
        return mBinding.root
    }

    private fun initView() {
        initVpTab()
        initClick()
    }

    private fun initClick() {
        with(mBinding) {
            mineFragmentMineTvFansNum.setOnClickListener {
                FollowListActivity.startAction(requireContext(),mUserId,FollowListActivity.FollowType.FANS)
            }
            mineFragmentMineTvFollowNum.setOnClickListener {
                FollowListActivity.startAction(requireContext(),mUserId,FollowListActivity.FollowType.FOLLOWS)
            }
        }
    }

    private fun initVpTab(){
        initVp()
        initTabLayout()
    }

    private fun initVp() {
        // tab + vp
        val fragmentVpAdapter = FragmentVpAdapter(this).apply {
            add { VideoFlowFragment.newInstance(VideoFlowFragment.MineType.TYPE_PUBLISH,mUserId) }
            add { VideoFlowFragment.newInstance(VideoFlowFragment.MineType.TYPE_LIKE,mUserId) }
        }
        with(mBinding.mineFragmentMineVp) {
            adapter = fragmentVpAdapter
            // 禁止用户自己滑动
            isUserInputEnabled = false
        }
        initTabLayout()
    }

    private fun initTabLayout() {
        // 取消tabLayout的长按点击事件
        with(mBinding.mineFragmentMineTabLayout) {
            isLongClickable = false
            // api 26 以上 设置空text
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.O) {
                tooltipText = ""
            }
            val titles = listOf("发布", "喜欢")
            //和viewpager联动起来
            TabLayoutMediator(this, mBinding.mineFragmentMineVp) { tab, position ->
                tab.text = titles[position]
            }.attach()
        }
    }

    override fun onStart() {
        super.onStart()
        getUserInfo()
    }

    private fun getUserInfo() {
        mViewModel.getUserInfo(mUserId)
    }

    private fun initObserve() {
        mViewModel.userInfo.collectLaunch {
            if (it != null){
                if (it.status_code == 0){
                    val userInfo = it.user
                    with(mBinding) {
                        mineFragmentMineTvUserName.text =userInfo.name
                        if (userInfo.avatar != "") mineFragmentMineImgUser.setImageFromUrl(userInfo.name)
                        mineFragmentMineTvDescribe.text = userInfo.signature
                        mineFragmentMineTvFollowNum.text = userInfo.follow_count.toString()
                        mineFragmentMineTvFansNum.text = userInfo.follower_count.toString()
                    }
                }else{
                    toast("网络错误")
                }
            }
        }
    }

    /**
     * 必须通过newInstance来调用，通过不同的userId来达到不同的效果
     */
    companion object{
        fun newInstance(userId : Long) = MineFragment().apply {
            arguments = bundleOf(
                this::mUserId.name to userId
            )
        }
    }
}