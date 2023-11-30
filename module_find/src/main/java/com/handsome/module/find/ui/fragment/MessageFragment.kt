package com.handsome.module.find.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.handsome.lib.util.base.BaseFragment
import com.handsome.module.find.databinding.FindFragmentMessageBinding

class MessageFragment : BaseFragment() {
    private val mBinding by lazy { FindFragmentMessageBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    companion object{
        fun newInstance() = MessageFragment()
    }
}