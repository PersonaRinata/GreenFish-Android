package com.handsome.module.find.ui.fragment

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
import com.handsome.module.find.bean.ContentType
import com.handsome.module.find.databinding.FindFragmentMessagePageBinding
import com.handsome.module.find.ui.activity.WebActivity
import com.handsome.module.find.ui.adapter.MessagePageAdapter
import com.handsome.module.find.ui.viewmodel.MessagePageFragmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 这个是内容列表的fragment，根据不同的类型获取不同的数据
 */
class MessagePageFragment : BaseFragment() {
    private val mBinding by lazy { FindFragmentMessagePageBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<MessagePageFragmentViewModel>()
    private val mAdapter by lazy { MessagePageAdapter() }
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
        mBinding.findFragmentMessagePageRv.apply {
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
        fun newInstance(type : ContentType) = MessagePageFragment().apply {
            arguments = bundleOf(
                this::mType.name to type
            )
        }
    }
}