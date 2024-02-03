package com.handsome.module.search.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.api.server.service.IMineService
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.invisible
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.extention.visible
import com.handsome.lib.util.service.impl
import com.handsome.module.search.R
import com.handsome.module.search.databinding.SearchFragmentFollowListBinding
import com.handsome.module.search.ui.adapter.FollowAdapter
import com.handsome.module.search.ui.viewmodel.FollowViewModel
import com.handsome.module.search.ui.viewmodel.SearchActivityViewModel

class FollowFragment : BaseFragment() {
    private val mBinding by lazy { SearchFragmentFollowListBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<FollowViewModel>()
    private val mParentViewModel by activityViewModels<SearchActivityViewModel>()
    private val mAdapter by lazy { FollowAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObserve()
        initRv()
        return mBinding.root
    }

    private fun getData(content : String) {
        mViewModel.searchUser(content).collectLaunch {
            mAdapter.submitData(it)
        }
    }

    private fun initRv() {
        with(mBinding.searchFragmentFollowListRv) {
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapter.apply {
                setOnClickFollow { user ->
                    mViewModel.followUser(user.id, if (user.is_follow) 2 else 1)
                }
                setOnClickUser {
                    IMineService::class.impl.startPersonActivity(it.id)
                }
                addLoadStateListener {
                    when (it.refresh) {
                        is LoadState.NotLoading -> {
                            if (mAdapter.itemCount == 0){
                                mBinding.searchFragmentFollowTvNotFound.visible()
                                mBinding.searchFragmentFollowListRv.invisible()
                                mBinding.root.setBackgroundResource(R.color.search_not_found_bg)
                            }else{
                                mBinding.searchFragmentFollowTvNotFound.invisible()
                                mBinding.searchFragmentFollowListRv.visible()
                                mBinding.root.setBackgroundResource(R.color.white)
                            }
                        }
                        is LoadState.Loading -> {}
                        is LoadState.Error -> {
                            toast("加载失败~")
                            val state = it.refresh as LoadState.Error
                            Log.d("lx","(state.error.message)-->>${state.error.message}")
                        }
                    }
                }
            }
        }
    }


    private fun initObserve() {

        mParentViewModel.searchContent.collectLaunch{ content ->
            if (content != null){
                getData(content)
            }
        }

        mViewModel.followUser.collectLaunch { data ->
            if (data != null) {
                if (data.isSuccess(requireActivity())) {
                    mParentViewModel.searchContent.value?.let { getData(it) }  //刷新数据，用数据来说话
                } else {
                    toast("网络异常")
                }
            }
        }
    }

    companion object {
        fun newInstance() = FollowFragment()
    }
}