package com.handsome.yiqu.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.databinding.MainFragmentVideoFlowBinding
import com.handsome.yiqu.ui.adapter.VideoForegroundAdapter
import com.handsome.yiqu.ui.viewmodel.fragment.VideoFlowViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 我的界面的发布和喜欢(❤ ω ❤)
 */
class VideoFlowFragment : BaseFragment() {
    private val mBinding by lazy { MainFragmentVideoFlowBinding.inflate(layoutInflater) }
    private val mType by arguments<MineType>()
    private val mViewModel by viewModels<VideoFlowViewModel>()
    private val mAdapter by lazy { VideoForegroundAdapter() }
    private val mUserId by arguments<Long>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initRv()
        initObserve()
        getData()
        return mBinding.root
    }

    private fun initRv() {
        with(mBinding.mainFragmentVideoFlowRv) {
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = mAdapter
        }
    }

    private fun getData() {
        when(mType){
            MineType.TYPE_LIKE -> {
                mViewModel.getLikeVideo(mUserId)
            }
            MineType.TYPE_PUBLISH -> {
                mViewModel.getUserPublish(mUserId)
            }
        }
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch(myCoroutineExceptionHandler){
            mViewModel.likeVideo.collectLatest {
                if (it != null){
                    if (it.status_code == 0){
                        mAdapter.submitList(it.video_list)
                    }else{
                        toast("网络错误")
                    }
                }
            }
            mViewModel.publishVideo.collectLatest {
                if (it != null){
                    if (it.status_code == 0){
                        mAdapter.submitList(it.video_list)
                    }else{
                        toast("网络错误")
                    }
                }
            }
        }
    }


    companion object{
        // 需要传入用户id，由本fragment自己请求数据
        fun newInstance(type : MineType,userId : Long) = VideoFlowFragment().apply {
            arguments = bundleOf(
                this::mType.name to type,
                this::mUserId.name to userId
            )
        }
    }

    enum class MineType{
        TYPE_PUBLISH,
        TYPE_LIKE
    }
}