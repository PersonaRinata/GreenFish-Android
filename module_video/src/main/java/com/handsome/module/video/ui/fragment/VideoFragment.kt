package com.handsome.module.video.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.handsome.api.video.bean.VideoBean
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.api.server.service.IMineService
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.gone
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.service.impl
import com.handsome.lib.util.util.shareText
import com.handsome.module.search.R
import com.handsome.module.search.databinding.VideoFragmentVideoBinding
import com.handsome.module.video.ui.dialog.CommentDialog
import com.handsome.module.video.ui.viewmodel.VideoFragmentViewModel
import com.handsome.module.video.util.makeLog
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videoplayer.player.BaseVideoView.SCREEN_SCALE_MATCH_PARENT

/**
 * 关注和喜欢系统
 *
 * 原本喜欢，喜欢数量100个，喜欢状态
 * 再次点击，喜欢数量99个，不喜欢状态
 * 再次点击，喜欢书香100个，喜欢状态
 *
 * 原本不喜欢，喜欢数量100个，不喜欢状态
 * 再次点击，喜欢数量101个，喜欢状态
 * 再次点击，喜欢书香100个，不喜欢状态
 */
class VideoFragment : BaseFragment() {
    private val mBinding by lazy { VideoFragmentVideoBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<VideoFragmentViewModel>()
    private val mUserInfo by lazy { IAccountService::class.impl.getUserInfo() }
    private val mVideo by arguments<VideoBean>()
    private var isLike: Boolean = false
    private var isFollow = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initVideo()
        initOthers()
    }


    private fun initOthers() {
        initBack()
        initUser()  //用户头像和名称
        initLike()
        initComment()
        initShare()
        initFollow()
        initTitle()
    }

    private fun initTitle() {
        mBinding.videoFragmentTvDescribe.text = mVideo.title
    }

    private fun initBack() {
        mBinding.videoFragmentImgBack.setOnClickListener {
            requireActivity().finishAfterTransition()
        }
    }

    private fun initObserve() {
        mViewModel.videoLike.collectLaunch {
            Log.d("lx", "videoFragment: like $it ")
            if (it != null) {
                if (it.isSuccess(requireActivity())) {
                    mBinding.videoFragmentTvLikeNumber.text = if (isLike) {
                        (mBinding.videoFragmentTvLikeNumber.text.toString().toInt() - 1).toString()
                    } else {
                        (mBinding.videoFragmentTvLikeNumber.text.toString().toInt() + 1).toString()
                    }
                    // 最后才改变操作之后的喜欢与不喜欢
                    isLike = !isLike
                    makeLog("islike=${isLike}")
                    setLikeBg(isLike)
                } else {
                    toast("点赞失败")
                }
            }
        }
        mViewModel.followUser.collectLaunch {
            Log.d("lx", "videoFragment: follow $it ")
            if (it != null) {
                if (it.isSuccess(requireActivity())) {
                    isFollow = !isFollow
                    setFollowBg(isFollow)
                } else {
                    toast("网络异常")
                }
            }
        }
    }

    private fun setFollowBg(isFollow: Boolean) {
        with(mBinding.videoFragmentTvFollow) {
            if (isFollow) {
                text = "已关注"
                setBackgroundResource(R.drawable.video_shape_follow_have_bg)
            } else {
                text = "关注"
                setBackgroundResource(R.drawable.video_shape_follow_bg)
            }
        }
    }

    private fun setLikeBg(isLike: Boolean) {
        with(mBinding.videoFragmentCommentImgLike) {
            if (isLike) {
                setImageResource(R.drawable.video_ic_like)
            } else {
                setImageResource(R.drawable.video_ic_unlike)
            }
        }
    }

    private fun initUser() {
        with(mBinding) {
            val pic = mVideo.author.avatar
            if (pic != "") videoFragmentImgUser.setImageFromUrl(pic)
            videoFragmentTvUserName.text = mVideo.author.nickname
            //点击事件，获取个人信息，获得到就跳转
            videoFragmentImgUser.setOnClickListener {
                IMineService::class.impl.startPersonActivity(mVideo.author.id)
            }
            videoFragmentTvUserName.setOnClickListener {
                IMineService::class.impl.startPersonActivity(mVideo.author.id)
            }
        }
    }

    private fun initFollow() {
        with(mBinding.videoFragmentTvFollow) {
            if (mUserInfo == null) {
                toast("您还没有登录哦~")
                return
            }
            if (mVideo.author.id == mUserInfo!!.user_id) {
                mBinding.videoFragmentTvFollow.gone()
                return
            }
            isFollow = mVideo.author.is_follow
            setFollowBg(isFollow)
            setOnClickListener {
                mViewModel.followUser(mVideo.author.id,isFollow)
            }
        }
    }

    private fun initShare() {
        mBinding.apply {
            videoFragmentImgShare.setOnClickListener {
                val shareText =
                    "作者：${mVideo.author} \n 描述：${mVideo.title} \n 视频链接：${mVideo.play_url}"
                requireActivity().shareText(shareText)
            }
        }
    }

    private fun initComment() {
        mBinding.apply {
            videoFragmentTvCommentNumber.text = mVideo.comment_count.toString()
            videoFragmentImgComment.setOnClickListener {
                CommentDialog.newInstance(mVideo.id).show(childFragmentManager, "CommentDialog")
            }
        }
    }

    private fun initLike() {
        with(mBinding) {
            videoFragmentTvLikeNumber.text = mVideo.favorite_count.toString()
            isLike = mVideo.is_favorite
            setLikeBg(isLike)
            with(videoFragmentCommentImgLike) {
                setOnClickListener {
                    mViewModel.updateLikeNum(mVideo.id, isLike)
                }
            }
        }
    }

    private fun initVideo() {
        mBinding.videoFragmentVideoView.apply {
            setUrl(mVideo.play_url) //设置视频地址
            setScreenScaleType(SCREEN_SCALE_MATCH_PARENT)
            val controller = StandardVideoController(requireContext())
            controller.addDefaultControlComponent(mVideo.title, false)
            setVideoController(controller) //设置控制器
        }
    }

    override fun onStop() {
        super.onStop()
        mBinding.videoFragmentVideoView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.videoFragmentVideoView.release()
    }

    companion object {
        @JvmStatic
        fun newInstance(video: VideoBean) = VideoFragment().apply {
            arguments = bundleOf(
                this::mVideo.name to video
            )
        }
    }
}