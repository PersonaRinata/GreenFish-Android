package com.handsome.module.record.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.handsome.module.record.databinding.RecordDialogSaveBackBinding

class SaveAndBackDialog(context : Context) : AlertDialog(context) {
    private val mBinding by lazy { RecordDialogSaveBackBinding.inflate(layoutInflater) }
    private var mOnClickConfirm : (()->Unit)? = null
    private var mOnClickCancel : (()->Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        initView()
    }

    private fun initView() {
        mBinding.recordDialogSaveBackConfirm.setOnClickListener {
            mOnClickConfirm?.invoke()
        }
        mBinding.recordDialogSaveBackCancel.setOnClickListener {
            mOnClickCancel?.invoke()
        }
    }

    fun setOnClickConfirm(onClickConfirm : ()->Unit){
        this.mOnClickConfirm = onClickConfirm
    }

    fun setOnClickCancel(onClickCancel : ()->Unit){
        this.mOnClickCancel = onClickCancel
    }

}