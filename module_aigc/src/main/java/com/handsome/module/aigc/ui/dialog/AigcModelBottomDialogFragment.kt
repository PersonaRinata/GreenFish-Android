package com.handsome.module.aigc.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.base.BaseBottomSheetDialogFragment
import com.handsome.module.aigc.R
import com.handsome.module.aigc.bean.DifferentModel
import com.handsome.module.aigc.bean.SingleModelBean
import com.handsome.module.aigc.databinding.AigcDialogChooseModelBinding
import com.handsome.module.aigc.ui.adapter.DiffModelAdapter

class AigcModelBottomDialogFragment : BaseBottomSheetDialogFragment() {
    private val mBinding by lazy { AigcDialogChooseModelBinding.inflate(layoutInflater) }
    private var mCurrentChooseModel by arguments<DifferentModel>()
    private val mAdapter by lazy {
        DiffModelAdapter().apply {
            setOnClickModel {
                mCurrentChooseModel = it
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        initClick()
        initRv()
        setData()
        return mBinding.root
    }

    private fun setData() {
        val list = arrayListOf<SingleModelBean>()
        list.add(SingleModelBean(R.color.white, DifferentModel.COMMON, false))
        list.add(
            SingleModelBean(
                R.drawable.aigc_ic_common_disease,
                DifferentModel.COMMON_DISEASE,
                false
            )
        )
        list.add(
            SingleModelBean(
                R.drawable.aigc_ic_heart_inner_disease,
                DifferentModel.HEART_INNER_DISEASE,
                false
            )
        )
        list.add(SingleModelBean(R.drawable.aigc_ic_eye, DifferentModel.EYE_DISEASE, false))
        list.add(
            SingleModelBean(
                R.drawable.aigc_ic_girl_disease,
                DifferentModel.GIRL_DISEASE,
                false
            )
        )
        list.add(SingleModelBean(R.drawable.aigc_ic_cancer, DifferentModel.CANCER_DISEASE, false))
        list.add(SingleModelBean(R.drawable.aigc_ic_medicine, DifferentModel.MEDICINE, false))
        list.add(
            SingleModelBean(
                R.drawable.aigc_ic_medical_machine,
                DifferentModel.MEDICAL_DEVICES,
                false
            )
        )
        val singleModelBeans = list.filter { it.enumModel == mCurrentChooseModel }
        if (singleModelBeans.isNotEmpty()) {
            singleModelBeans[0].isChoose = true
        } else {
            list[0].isChoose = true
        }
        mAdapter.submitList(list)
    }

    private fun initRv() {
        with(mBinding.aigcDialogChooseModelRv) {
            layoutManager = GridLayoutManager(requireContext(), 4, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
    }

    private var mOnClickConfirm: ((DifferentModel) -> Unit)? = null

    fun setOnClickConfirm(onClickConfirm: (DifferentModel) -> Unit) {
        this.mOnClickConfirm = onClickConfirm
    }

    private fun initClick() {
        mBinding.aigcDialogChooseModelTvConfirm.setOnClickListener {
            mOnClickConfirm?.invoke(mCurrentChooseModel)
        }
    }

    companion object {
        fun newInstance(currentModel: DifferentModel) = AigcModelBottomDialogFragment().apply {
            arguments = bundleOf(
                this::mCurrentChooseModel.name to currentModel
            )
        }
    }
}