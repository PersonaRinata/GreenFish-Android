package com.handsome.module.publish.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.handsome.module.publish.databinding.PublishDialogSuccessAndExitBinding

class SuccessAndExitDialog(context: Context) : AlertDialog(context) {
    private val mBinding by lazy { PublishDialogSuccessAndExitBinding.inflate(layoutInflater) }

    private var mOnClickConfirm : (()->Unit)? = null

    fun setOnClickConfirm(onClickConfirm : ()->Unit){
        this.mOnClickConfirm = onClickConfirm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        initClick()
    }

    private fun initClick() {
        mBinding.publishDialogSuccessBtnConfirm.setOnClickListener {
            mOnClickConfirm?.invoke()
        }
    }

}