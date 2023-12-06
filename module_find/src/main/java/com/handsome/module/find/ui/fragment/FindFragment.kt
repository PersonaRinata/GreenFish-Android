package com.handsome.module.find.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayoutMediator
import com.handsome.lib.api.server.MAIN_FIND
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.api.server.service.ILoginService
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.service.impl
import com.handsome.module.find.databinding.FindFragmentFindBinding

@Route(path = MAIN_FIND)
class FindFragment : BaseFragment() {
    private val mBinding by lazy { FindFragmentFindBinding.inflate(layoutInflater) }
    private val mUserInfo by lazy { IAccountService::class.impl.getUserInfo() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 需要整个TabLayout，然后整俩fragment
        if (mUserInfo == null) run {
            ILoginService::class.impl.startLoginActivity()
            requireActivity().finishAfterTransition()
        }
        initView()
        return mBinding.root
    }

    private fun initView() {
        initVpTab()
    }

    private fun initVpTab(){
        initVp()
        initTabLayout()
    }

    private fun initVp() {
        // tab + vp
        val fragmentVpAdapter = FragmentVpAdapter(this).apply {
            add { VideoFlowFragment.newInstance() }
            add { MessageFragment.newInstance() }
        }
        with(mBinding.findFragmentFindVp) {
            adapter = fragmentVpAdapter
            // 禁止用户自己滑动
            isUserInputEnabled = false
        }
        initTabLayout()
    }

    private fun initTabLayout() {
        // 取消tabLayout的长按点击事件
        with(mBinding.findFragmentFindTab) {
            isLongClickable = false
            // api 26 以上 设置空text
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.O) {
                tooltipText = ""
            }
            val titles = listOf("视频", "资讯")
            //和viewpager联动起来
            TabLayoutMediator(this, mBinding.findFragmentFindVp) { tab, position ->
                tab.text = titles[position]
            }.attach()
        }
    }
}