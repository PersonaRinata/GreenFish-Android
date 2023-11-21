package com.handsome.module.main.ui.fragment

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
import com.handsome.module.main.R
import com.handsome.module.main.bean.VideoBean
import com.handsome.module.main.databinding.MainFragmentVideoBinding
import com.handsome.module.main.ui.activity.PersonActivity
import com.handsome.module.main.ui.dialog.CommentDialog
import com.handsome.module.main.ui.viewmodel.fragment.VideoFragmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videoplayer.player.BaseVideoView.SCREEN_SCALE_MATCH_PARENT

class VideoFragment : BaseFragment() {
    private val mBinding by lazy { MainFragmentVideoBinding.inflate(layoutInflater) }
    private val myVideo by arguments<VideoBean>()
    private val mViewModel by viewModels<VideoFragmentViewModel>()
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
            mViewModel.videoComment.collectLatest {
                Log.d("lx", "videoFragment: observe${it}}")
                if (it != null){
                    if (it.status_code == 0){
                        CommentDialog(myVideo,it).show(childFragmentManager,"CommentDialog")
                    }else{
                        toast("网络异常")
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.videoLike.collectLatest {
                Log.d("lx", "videoFragment: like $it ")
                if (it != null){
                    if (it.status_code == 0){
                        with(mBinding.mainItemCommentImgLike){
                            if (!isLike) {
                                setImageResource(R.drawable.main_ic_like)
                                likeNumber++
                            } else {
                                setImageResource(R.drawable.main_ic_unlike)
                                likeNumber--
                            }
                            isLike = !isLike
                            mBinding.mainVideoTvLikeNumber.text = likeNumber.toString()
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
                        with(mBinding.mainVideoTvFollow){
                            if (!isFollow){
                                text = "已关注"
                                setBackgroundResource(R.drawable.main_shape_follow_have_bg)
                            }else{
                                text = "关注"
                                setBackgroundResource(R.drawable.main_shape_follow_no_bg)
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
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.userInfo.collectLatest {
                Log.d("lx", "videoFragment: userInfo $it ")
                if (it != null){
                    if(it.status_code == 0){
                        PersonActivity.startAction(requireContext(),it.user)
                    }else{
                        toast("网络异常")
                    }
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
            val pic = myVideo.author.avatar
            if (pic != "") mainVideoImgUser.setImageFromUrl(pic)
            mainVideoTvUserName.text = myVideo.author.name
            //点击事件，获取个人信息，获得到就跳转
            mainVideoImgUser.setOnClickListener {
                mViewModel.getUserInfo(myVideo.author.id)
            }
            mainVideoTvUserName.setOnClickListener {
                mViewModel.getUserInfo(myVideo.author.id)
            }
        }
    }

    private fun initFollow() {
        with(mBinding.mainVideoTvFollow){
            isFollow = myVideo.author.is_follow
            if (isFollow){
                text = "已关注"
                setBackgroundResource(R.drawable.main_shape_follow_have_bg)
            }else{
                text = "关注"
                setBackgroundResource(R.drawable.main_shape_follow_no_bg)
            }
            setOnClickListener {
                if(isCanFollow){
                    mViewModel.followUser(myVideo.author.id, actionId = if (isFollow) 2 else 1)
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
            mainVideoTvShareNumber.text = shareNum.toString()
            mainVideoImgShare.setOnClickListener {
                val shareText = "作者：${myVideo.author} \n 描述：${myVideo.title} \n 视频链接：${myVideo.play_url}"
                requireActivity().shareText(shareText)
                shareNum ++
                mainVideoTvShareNumber.text = shareNum.toString()
            }
        }
    }

    private fun initComment() {
        mBinding.apply {
            mainVideoTvCommentNumber.text = myVideo.comment_count.toString()
            mainVideoImgComment.setOnClickListener {
                mViewModel.getVideoComment(myVideo.id)
            }
        }
    }

    private fun initLike() {
        likeNumber = myVideo.favorite_count.toLong()
        mBinding.apply {
            mainVideoTvLikeNumber.text = likeNumber.toString()
            mainItemCommentImgLike.apply {
                isLike = myVideo.is_favorite
                if (isLike) {
                    setImageResource(R.drawable.main_ic_like)
                } else {
                    setImageResource(R.drawable.main_ic_unlike)
                }
                setOnClickListener {
                    if(isCanLike){
                        // 1 是喜欢， 2 是不喜欢
                        mViewModel.updateLikeNum(myVideo.id,if (!isLike) 1 else 2)
                        isCanLike = false
                    }else{
                        toast("点赞太频繁了哦~")
                    }
                }
            }
        }
    }

    private fun initVideo() {
        mBinding.mainVideoVideo.apply {
            setUrl(myVideo.play_url) //设置视频地址
            setScreenScaleType(SCREEN_SCALE_MATCH_PARENT)
            val controller = StandardVideoController(requireContext())
            controller.addDefaultControlComponent(myVideo.title, false)
            setVideoController(controller) //设置控制器
        }
    }

    override fun onStop() {
        super.onStop()
        mBinding.mainVideoVideo.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.mainVideoVideo.release()
    }

    companion object {
        @JvmStatic
        fun newInstance(video:VideoBean) = VideoFragment().apply {
            arguments = bundleOf(
                this::myVideo.name to video
            )
        }
    }
}