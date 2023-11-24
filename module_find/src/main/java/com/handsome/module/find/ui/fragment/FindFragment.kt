package com.handsome.module.find.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.MAIN_FIND
import com.handsome.lib.util.base.BaseFragment
import com.handsome.module.find.databinding.FindFragmentFindBinding
import com.handsome.module.find.ui.viewmodel.FindViewModel

@Route(path = MAIN_FIND)
class FindFragment : BaseFragment() {
    private val mBinding by lazy { FindFragmentFindBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<FindViewModel>()
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
        getData()
    }

    private fun getData() {
        mViewModel.getVideo()
    }

    private fun initObserve() {
        mViewModel.videoBean.observe(viewLifecycleOwner){
            Log.d("lx", "findFragment:${it} ")
        }
    }
}