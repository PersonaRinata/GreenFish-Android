package com.handsome.module.main.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.main.databinding.MainFragmentRecommendBinding
import com.handsome.module.main.ui.activity.SearchActivity
import com.handsome.module.main.ui.viewmodel.fragment.RecommendFragmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecommendFragment : BaseFragment() {
    private val mBinding by lazy { MainFragmentRecommendBinding.inflate(layoutInflater) }
    private val mViewModel by lazy { ViewModelProvider(this)[RecommendFragmentViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObserve()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getVideoData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initClick()
    }

    private fun getVideoData() {
        mViewModel.getVideoList()
    }

    private fun initObserve() {
        val fragmentVpAdapter = FragmentVpAdapter(this)
        viewLifecycleOwner.lifecycleScope.launch(myCoroutineExceptionHandler) {
            mViewModel.videoList.collectLatest {
                if (it != null){
                    if (it.status_code == 0){
                        it.video_list.forEach {video ->
                            fragmentVpAdapter.add {
                                VideoFragment.newInstance(video)
                            }
                        }
                        mBinding.mainRecommendVpVideoList.adapter = fragmentVpAdapter
                    }else{
                        toast("网络错误")
                    }
                }
            }
        }
    }

    private fun initClick() {
        // 搜索框的点击事件
        mBinding.mainRecommendLinearSearch.setOnClickListener {
            SearchActivity.startAction(requireContext())
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = RecommendFragment()
    }
}