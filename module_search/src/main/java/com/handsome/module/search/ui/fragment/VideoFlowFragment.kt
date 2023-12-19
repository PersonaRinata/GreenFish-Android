package com.handsome.module.search.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.handsome.api.video.service.IVideoService
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.invisible
import com.handsome.lib.util.extention.visible
import com.handsome.lib.util.service.impl
import com.handsome.module.search.R
import com.handsome.module.search.databinding.SearchFragmentVideoFlowBinding
import com.handsome.module.search.ui.adapter.VideoForegroundAdapter
import com.handsome.module.search.ui.viewmodel.SearchActivityViewModel
import com.handsome.module.search.ui.viewmodel.VideoFlowViewModel

/**
 * 我的界面的发布和喜欢(❤ ω ❤)
 */
class VideoFlowFragment : BaseFragment() {
    private val mBinding by lazy { SearchFragmentVideoFlowBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<VideoFlowViewModel>()
    private val mParentViewModel by activityViewModels<SearchActivityViewModel>()
    private val mAdapter by lazy { VideoForegroundAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initRv()
        initObserve()
        return mBinding.root
    }

    private fun initRv() {
        mAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    if (mAdapter.itemCount != 0){
                        mBinding.searchFragmentVideoFlowNotFound.invisible()
                        mBinding.searchFragmentVideoFlowRv.visible()
                        mBinding.root.setBackgroundResource(R.color.white)
                    }else{
                        mBinding.searchFragmentVideoFlowNotFound.visible()
                        mBinding.searchFragmentVideoFlowRv.invisible()
                        mBinding.root.setBackgroundResource(R.color.search_not_found_bg)
                    }
                }
                is LoadState.Loading -> {}
                is LoadState.Error -> {
                    mBinding.searchFragmentVideoFlowNotFound.visible()
                    mBinding.searchFragmentVideoFlowRv.invisible()
                    mBinding.root.setBackgroundResource(R.color.search_not_found_bg)
                    val state = it.refresh as LoadState.Error
                    Log.d("lx","(state.error.message)-->>${state.error.message}")
                }
            }
        }
        with(mBinding.searchFragmentVideoFlowRv) {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = mAdapter.apply {
                setOnClickVideo {
                    IVideoService::class.impl.startVideoActivity(it)
                }
            }
        }
    }

    private fun getData(content : String) {
        mViewModel.searchVideo(content).collectLaunch {
            mAdapter.submitData(it)
        }
    }

    private fun initObserve() {
        mParentViewModel.searchContent.collectLaunch {
            if (it != null){
                getData(it)
            }
        }
    }



    companion object {
        fun newInstance() = VideoFlowFragment()
    }
}