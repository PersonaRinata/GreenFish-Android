package com.handsome.module.find.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.handsome.api.video.service.IVideoService
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.service.impl
import com.handsome.module.find.databinding.FindFragmentVideoFlowBinding
import com.handsome.module.find.ui.adapter.VideoForegroundAdapter
import com.handsome.module.find.ui.viewmodel.VideoFlowViewModel

/**
 * 我的界面的发布和喜欢(❤ ω ❤)
 */
class VideoFlowFragment : BaseFragment() {
    private val mBinding by lazy { FindFragmentVideoFlowBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<VideoFlowViewModel>()
    private val mAdapter by lazy { VideoForegroundAdapter() }

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
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mAdapter.apply {
                setOnClickVideo {
                    IVideoService::class.impl.startVideoActivity(it)
                }
            }
        }
    }

    private fun getData() {
        mViewModel.getFindVideo()
    }

    private fun initObserve() {
        mViewModel.findVideo.observe(viewLifecycleOwner){
            if (it.status_code == 0){
                mAdapter.submitList(it.video_list)
            }else{
                toast("网络错误")
            }
        }
    }


    companion object {
        // 需要传入用户id，由本fragment自己请求数据
        fun newInstance() = VideoFlowFragment()
    }
}