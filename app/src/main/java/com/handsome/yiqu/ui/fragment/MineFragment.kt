package com.handsome.yiqu.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.gone
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.extention.visible
import com.handsome.yiqu.R
import com.handsome.yiqu.bean.AuthorBean
import com.handsome.yiqu.databinding.MainFragmentMineBinding
import com.handsome.yiqu.net.ApiService
import com.handsome.yiqu.ui.activity.FollowListActivity
import com.handsome.yiqu.ui.viewmodel.fragment.MineFragmentViewModel

class MineFragment : BaseFragment() {
    private val mBinding by lazy { MainFragmentMineBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<MineFragmentViewModel>()
    private var mUserInfo by arguments<AuthorBean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        return mBinding.root
    }

    private fun initView() {
        //todo 如果用户id不等于本身的id，那么就显示关注按钮
        mBinding.mainFragmentMineTvFollow.visible()
        initTvFollow()
        initImgUser()
        initTvUserName()
        initTvFansNum()
        initTvFollowNum()
        initVp()
        initTabLayout()
    }

    private fun initTvFollowNum() {
        with(mBinding.mainFragmentMineTvFollowNum){
            val followCount = "关注：${mUserInfo.follow_count}"
            text = followCount
            setOnClickListener {
                FollowListActivity.startAction(requireContext(),mUserInfo,FollowListActivity.FollowType.FOLLOWS)
            }
        }
    }

    private fun initTvFansNum() {
        with(mBinding.mainFragmentMineTvFansNum){
            val followerCount = "粉丝：${mUserInfo.follower_count}"
            text = followerCount
            setOnClickListener {
                FollowListActivity.startAction(requireContext(),mUserInfo,FollowListActivity.FollowType.FANS)
            }
        }
    }

    /**
     * 只会调用一次，所以倒也无所谓
     */
    private fun initTvFollow() {
        // todo 等待做判断是不是本人
            // 不是本人
            with(mBinding.mainFragmentMineTvFollow) {
                if (mUserInfo.id != ApiService.userId) {
                    // 初始化follow按钮的颜色
                    if (mUserInfo.is_follow) {
                        text = "已关注"
                        setBackgroundResource(R.drawable.main_shape_follow_have_bg)
                    } else {
                        text = "关注"
                        setBackgroundResource(R.drawable.main_shape_follow_no_bg)
                    }
                    setOnClickListener {
                        // action_type 2 为 取消关注  1 为 关注
                        mViewModel.followUser(mUserInfo.id, actionId = if (mUserInfo.is_follow) 2 else 1)
                    }
                }else{
                    gone()
                }
            }

    }

    private fun initTvUserName() {
        with(mBinding.mainFragmentMineTvUserName) {
            text = mUserInfo.name
            setOnClickListener {
                //todo 用户名称的点击事件，跳转
                toast("用户名称的点击事件")
            }
        }
    }

    private fun initImgUser() {
        with(mBinding.mainFragmentMineImgUser) {
            if (mUserInfo.avatar != "") {
                setImageFromUrl(mUserInfo.avatar)
            }
            setOnClickListener {
                //todo 头像的点击事件，跳转
                toast("头像的点击事件，还没做！！！")
            }
        }
    }

    private fun initVp() {
        // tab + vp
        val fragmentVpAdapter = FragmentVpAdapter(this).apply {
            add { VideoFlowFragment.newInstance(VideoFlowFragment.MineType.TYPE_PUBLISH,mUserInfo.id) }
            add { VideoFlowFragment.newInstance(VideoFlowFragment.MineType.TYPE_LIKE,mUserInfo.id) }
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

    companion object {

        @JvmStatic
        fun newInstance(userBean: AuthorBean) = MineFragment().apply {
            arguments = bundleOf(
                this::mUserInfo.name to userBean
            )
        }
    }
}