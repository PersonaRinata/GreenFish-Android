package com.handsome.module.find.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
        initRefresh()
        initObserve()
        getData()
        return mBinding.root
    }

    private fun initRefresh() {
        mBinding.findFragmentVideoFlowRefresh.setOnRefreshListener {
            getData()
        }
    }

    private fun initRv() {
        with(mBinding.mainFragmentVideoFlowRv) {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            }
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
            if (it.isSuccess(requireActivity())){
                if (!it.video_list.isNullOrEmpty()){
                    mAdapter.submitList(it.video_list)
                    mBinding.findFragmentVideoFlowRefresh.isRefreshing = false
                }
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