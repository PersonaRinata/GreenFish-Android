package com.handsome.yiqu.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.extention.visible
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.R
import com.handsome.yiqu.bean.AuthorBean
import com.handsome.yiqu.databinding.MainFragmentMineBinding
import com.handsome.yiqu.ui.viewmodel.fragment.MineFragmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MineFragment : BaseFragment() {
    private val mBinding by lazy { MainFragmentMineBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<MineFragmentViewModel>()
    private var mUserInfo: AuthorBean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        initObserve()
        getUserInfo()
        return mBinding.root
    }

    private fun initView() {
        //todo 如果用户id不等于本身的id，那么就显示关注按钮
        mBinding.mainFragmentMineTvFollow.visible()
        initTvFollow()
        initImgUser()
        initTvUserName()
        initVp()
        initTabLayout()
    }

    /**
     * 只会调用一次，所以倒也无所谓
     */
    private fun initTvFollow() {
        // todo 等待做判断是不是本人
        with(mBinding.mainFragmentMineTvFollow) {
            setOnClickListener {
                // action_type 2 为 取消关注  1 为 关注
                if (mUserInfo != null) {
                    mViewModel.followUser(
                        mUserInfo!!.id,
                        actionId = if (mUserInfo!!.is_follow) 2 else 1
                    )
                }
            }
        }
    }

    private fun initTvUserName() {
        with(mBinding.mainFragmentMineTvUserName) {
            setOnClickListener {
                //todo 用户名称的点击事件，跳转
                toast("用户名称的点击事件")
            }
        }
    }

    private fun initImgUser() {
        with(mBinding.mainFragmentMineImgUser) {
            setOnClickListener {
                //todo 头像的点击事件，跳转
                toast("头像的点击事件，还没做！！！")
            }
        }
    }

    private fun initVp() {
        // tab + vp
        val fragmentVpAdapter = FragmentVpAdapter(this).apply {
            add { VideoFlowFragment.newInstance(VideoFlowFragment.MineType.TYPE_PUBLISH) }
            add { VideoFlowFragment.newInstance(VideoFlowFragment.MineType.TYPE_LIKE) }
        }
        with(mBinding.mainFragmentMineVp) {
            adapter = fragmentVpAdapter
            // 禁止用户自己滑动
            isUserInputEnabled = false
        }
    }

    private fun initTabLayout() {
        // 取消tabLayout的长按点击事件
        with(mBinding.mainFragmentMineTabLayout) {
            isLongClickable = false
            // api 26 以上 设置空text
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                tooltipText = ""
            }
            val titles = listOf("发布", "喜欢")
            //和viewpager联动起来
            TabLayoutMediator(this, mBinding.mainFragmentMineVp) { tab, position ->
                tab.text = titles[position]
            }.attach()
        }
    }

    private fun getUserInfo() {
        mViewModel.getUserInfo()
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch(myCoroutineExceptionHandler) {
            mViewModel.userInfo.collectLatest {
                Log.d("lx", "initObserve:${it} ")
                if (it != null) {
                    if (it.status_code == 0) {
                        mUserInfo = it
                        with(mBinding) {
                            with(mBinding.mainFragmentMineTvFollow) {
                                // 初始化follow按钮的颜色
                                if (it.is_follow) {
                                    text = "已关注"
                                    setBackgroundResource(R.drawable.main_shape_follow_have_bg)
                                } else {
                                    text = "关注"
                                    setBackgroundResource(R.drawable.main_shape_follow_no_bg)
                                }
                            }
                            if (it.avatar != null && it.avatar != "") {
                                mainFragmentMineImgUser.setImageFromUrl(it.avatar)
                            }
                            mainFragmentMineTvUserName.text = it.name ?: "开心超人"
                            val followCount = "关注：${it.follow_count}"
                            mainFragmentMineTvFollowNum.text = followCount
                            val followerCount = "粉丝：${it.follower_count}"
                            mainFragmentMineTvFansNum.text = followerCount
                            mainFragmentMineTvDescribe.text = it.signature ?: "等待填写签名"
                            if (it.is_follow) {
                                with(mainFragmentMineTvFollow) {
                                    setBackgroundResource(R.drawable.main_shape_follow_have_bg)
                                    text = "已关注"
                                }
                            }
                        }
                    } else {
                        toast("网络错误")
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()

        @JvmStatic
        fun newInstance(authorBean: AuthorBean) = MineFragment().apply {
            //todo
        }
    }
}