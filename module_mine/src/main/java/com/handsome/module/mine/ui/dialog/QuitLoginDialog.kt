package com.handsome.module.mine.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.handsome.lib.util.base.BaseDialogFragment
import com.handsome.module.mine.databinding.MineDialogQuitLoginBinding

class QuitLoginDialog : BaseDialogFragment() {
    private val mBinding by lazy { MineDialogQuitLoginBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initClick()
        return mBinding.root
    }

    private var mOnClickCancel: (() -> Unit)? = null

    fun setOnClickCancel(onClickCancel: () -> Unit) {
        this.mOnClickCancel = onClickCancel
    }

    private var mOnClickConfirm: (() -> Unit)? = null

    fun setOnClickConfirm(onClickConfirm: () -> Unit) {
        this.mOnClickConfirm = onClickConfirm
    }

    private fun initClick() {
        mBinding.mineDialogQuitLoginTvCancel.setOnClickListener {
            mOnClickCancel?.invoke()
        }
        mBinding.mineDialogQuitLoginTvConfirm.setOnClickListener {
            mOnClickConfirm?.invoke()
        }
    }

    companion object{
        fun newInstance() = QuitLoginDialog()
    }

}