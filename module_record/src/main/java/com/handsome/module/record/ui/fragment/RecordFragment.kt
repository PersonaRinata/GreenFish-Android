package com.handsome.module.record.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.MAIN_RECORD
import com.handsome.lib.util.base.BaseFragment
import com.handsome.module.record.databinding.RecordFramentRecordBinding

@Route(name = MAIN_RECORD, path = MAIN_RECORD)
class RecordFragment : BaseFragment() {
    private val mBinding by lazy { RecordFramentRecordBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object{
        fun newInstance() = RecordFragment()
    }
}