package com.handsome.module.aigc.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.handsome.lib.util.base.BaseFragment
import com.handsome.module.aigc.databinding.AigcFragmentAigcBinding

class AigcFragment : BaseFragment() {
    private val mBinding by lazy { AigcFragmentAigcBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        initClick()
        initObserve()
        return mBinding.root
    }


    private fun initView() {

    }

    private fun initClick() {
        mBinding.aigcFragmentAigcEt.setOnEditorActionListener{ _,actionId,_ ->
            if(actionId == EditorInfo.IME_ACTION_SEND){
                val text = mBinding.aigcFragmentAigcEt.text
                true
            }else{
                false
            }
        }
    }


    private fun initObserve() {

    }
}