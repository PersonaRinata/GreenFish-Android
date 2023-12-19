package com.handsome.module.video.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.shareText
import com.handsome.module.search.R
import com.handsome.module.search.databinding.VideoFragmentVideoBinding
import com.handsome.api.video.bean.VideoBean
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.api.server.service.IMineService
import com.handsome.lib.util.extention.gone
import com.handsome.lib.util.service.impl
import com.handsome.module.video.ui.dialog.CommentDialog
import com.handsome.module.video.ui.viewmodel.VideoFragmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videoplayer.player.BaseVideoView.SCREEN_SCALE_MATCH_PARENT

class VideoFragment : BaseFragment() {
    private val mBinding by lazy { VideoFragmentVideoBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<VideoFragmentViewModel>()
    private val mUserInfo by lazy { IAccountService::class.impl.getUserInfo() }
    private val mVideo by arguments<VideoBean>()
    private var likeNumber : Long = 0L
    private var isLike : Boolean = false
    private var isCanLike : Boolean = true

    private var isFollow = false
    private var isCanFollow : Boolean = true


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

    private fun initObserve() {

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.videoLike.collectLatest {
                Log.d("lx", "videoFragment: like $it ")
                if (it != null){
                    if (it.status_code == 0){
                        with(mBinding.videoFragmentCommentImgLike){
                            if (!isLike) {
                                setImageResource(R.drawable.video_ic_like)
                                likeNumber++
                            } else {
                                setImageResource(R.drawable.video_ic_unlike)
                                likeNumber--
                            }
                            isLike = !isLike
                            mBinding.videoFragmentTvLikeNumber.text = likeNumber.toString()
                        }
                    }else{
                        toast("点赞失败")
                    }
                    isCanLike = true
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.followUser.collectLatest {
                Log.d("lx", "videoFragment: follow $it ")
                if (it != null){
                    if(it.status_code == 0){
                        with(mBinding.videoFragmentTvFollow){
                            if (!isFollow){
                                text = "已关注"
                                setBackgroundResource(R.drawable.video_shape_follow_have_bg)
                            }else{
                                text = "关注"
                                setBackgroundResource(R.drawable.video_shape_follow_bg)
                            }
                            isFollow = !isFollow
                        }
                    }else{
                        toast("网络异常")
                    }
                    isCanFollow = true
                }
            }
        }
    }

    private fun initOthers() {
        initUser()  //用户头像和名称
        initLike()
        initComment()
        initShare()
        initFollow()
    }

    private fun initUser() {
        with(mBinding){
            val pic = mVideo.author.avatar
            if (pic != "") videoFragmentImgUser.setImageFromUrl(pic)
            videoFragmentTvUserName.text = mVideo.author.name
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
        with(mBinding.videoFragmentTvFollow){
            if (mUserInfo == null){
                toast("您还没有登录哦~")
                return
            }
            if (mVideo.id == mUserInfo!!.user_id){
                mBinding.videoFragmentTvFollow.gone()
                return
            }
            isFollow = mVideo.author.is_follow
            if (isFollow){
                text = "已关注"
                setBackgroundResource(R.drawable.video_shape_follow_have_bg)
            }else{
                text = "关注"
                setBackgroundResource(R.drawable.video_shape_follow_bg)
            }
            setOnClickListener {
                if(isCanFollow){
                    mViewModel.followUser(mVideo.author.id, actionId = if (isFollow) 2 else 1)
                    isCanFollow = false
                }else{
                    toast("关注太频繁了哦~")
                }
            }
        }
    }

    private fun initShare() {
        mBinding.apply {
            var shareNum = 0
            videoFragmentTvShareNumber.text = shareNum.toString()
            videoFragmentImgShare.setOnClickListener {
                val shareText = "作者：${mVideo.author} \n 描述：${mVideo.title} \n 视频链接：${mVideo.play_url}"
                requireActivity().shareText(shareText)
                shareNum ++
                videoFragmentTvShareNumber.text = shareNum.toString()
            }
        }
    }

    private fun initComment() {
        mBinding.apply {
            videoFragmentTvCommentNumber.text = mVideo.comment_count.toString()
            videoFragmentImgComment.setOnClickListener {
                CommentDialog.newInstance(mVideo.id).show(childFragmentManager,"CommentDialog")
            }
        }
    }

    private fun initLike() {
        likeNumber = mVideo.favorite_count.toLong()
        mBinding.apply {
            videoFragmentTvLikeNumber.text = likeNumber.toString()
            videoFragmentCommentImgLike.apply {
                isLike = mVideo.is_favorite
                if (isLike) {
                    setImageResource(R.drawable.video_ic_like)
                } else {
                    setImageResource(R.drawable.video_ic_unlike)
                }
                setOnClickListener {
                    if(isCanLike){
                        // 1 是喜欢， 2 是不喜欢
                        mViewModel.updateLikeNum(mVideo.id,if (!isLike) 1 else 2)
                        isCanLike = false
                    }else{
                        toast("点赞太频繁了哦~")
                    }
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