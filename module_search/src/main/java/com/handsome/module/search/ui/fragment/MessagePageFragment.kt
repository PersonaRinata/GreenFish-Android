package com.handsome.module.search.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.invisible
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.extention.visible
import com.handsome.module.search.R
import com.handsome.module.search.databinding.SearchFragmentMessagePageBinding
import com.handsome.module.search.ui.activity.WebActivity
import com.handsome.module.search.ui.adapter.MessagePageAdapter
import com.handsome.module.search.ui.viewmodel.MessagePageFragmentViewModel
import com.handsome.module.search.ui.viewmodel.SearchActivityViewModel

/**
 * 这个是内容列表的fragment，根据不同的类型获取不同的数据
 */
class MessagePageFragment : BaseFragment() {
    private val mBinding by lazy { SearchFragmentMessagePageBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<MessagePageFragmentViewModel>()
    private val mParentViewModel by activityViewModels<SearchActivityViewModel>()
    private val mAdapter by lazy { MessagePageAdapter() }

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
        mBinding.searchFragmentMessagePageRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter.apply {
                setOnClickItem {
                    WebActivity.startAction(requireContext(),it.url)
                }
            }
        }
    }


    private fun getData(content : String) {
        mViewModel.getContentList(content)
    }

    private fun initObserve() {
        mParentViewModel.searchContent.collectLaunch {
            if (it != null){
                getData(it)
            }
        }
        mViewModel.contentList.collectLaunch {
            if (it != null){
                if (it.isSuccess(requireActivity())){
                    if (it.contentList.isNotEmpty()){
                        mBinding.searchFragmentMessagePageRv.visible()
                        mBinding.searchFragmentMessagePageTvNotFound.invisible()
                        mBinding.root.setBackgroundResource(R.color.white)
                        mAdapter.submitList(it.contentList)
                    }else{
                        mBinding.searchFragmentMessagePageRv.invisible()
                        mBinding.searchFragmentMessagePageTvNotFound.visible()
                        mBinding.root.setBackgroundResource(R.color.search_not_found_bg)
                    }
                }else{
                    toast("服务异常，请重试~")
                }
            }
        }
    }

    companion object{
        // 内容的类型，不同值代表不同类型
        fun newInstance() = MessagePageFragment().apply {
            arguments = bundleOf()
        }

        const val TYPE_ALL = "全部"
        const val TYPE_FOOD = "食物"
        const val TYPE_LIFE = "生活"
        const val TYPE_MEDICINE = "药品"
    }
}