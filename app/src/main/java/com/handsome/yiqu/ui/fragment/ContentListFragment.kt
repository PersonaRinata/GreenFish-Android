package com.handsome.yiqu.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.ContentType
import com.handsome.yiqu.databinding.MainFragmentContentListBinding
import com.handsome.yiqu.ui.activity.WebActivity
import com.handsome.yiqu.ui.adapter.ContentListAdapter
import com.handsome.yiqu.ui.viewmodel.fragment.ContentListFragmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 这个是内容列表的fragment，根据不同的类型获取不同的数据
 */
class ContentListFragment : BaseFragment() {
    private val mBinding by lazy { MainFragmentContentListBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<ContentListFragmentViewModel>()
    private val mAdapter by lazy { ContentListAdapter() }
    private val mType by arguments<ContentType>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initRv()
        initObserve()
        getDate()
        return mBinding.root
    }

    private fun initRv() {
        mBinding.mainFragmentContentListRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter.apply {
                setOnClickItem {
                    WebActivity.startAction(requireContext(),it.url)
                }
            }
        }
    }

    private fun getDate() {
        mViewModel.getContentList(mType)
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch(myCoroutineExceptionHandler) {
            mViewModel.contentList.collectLatest { data  ->
                if (data != null){
                    mAdapter.submitList(data.contentList)
                }
            }
        }
    }

    companion object{
        // 内容的类型，不同值代表不同类型
        fun newInstance(type : ContentType) = ContentListFragment().apply {
            arguments = bundleOf(
                this::mType.name to type
            )
        }
    }
}