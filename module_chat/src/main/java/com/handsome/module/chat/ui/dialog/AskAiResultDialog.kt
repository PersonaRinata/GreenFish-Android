package com.handsome.module.chat.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.handsome.lib.util.base.BaseDialogFragment
import com.handsome.lib.util.extention.toast
import com.handsome.module.chat.databinding.ChatDialogAskAiResultBinding
import com.handsome.module.chat.ui.viewmodel.AskAiDialogViewModel

class AskAiResultDialog : BaseDialogFragment() {
    private val mBinding by lazy { ChatDialogAskAiResultBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<AskAiDialogViewModel>()
    private val mContent : String by arguments()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initView()
        askAI(mContent)
        initObserve()
        return mBinding.root
    }

    private fun initObserve() {
        mViewModel.askAi.collectLaunch {
            if (it != null){
                if (it.isSuccess(requireActivity())){
                    setAiText(it.msg)
                }else{
                    toast("网络异常，请稍后重试~")
                }
            }
        }
    }

    private fun askAI(content : String) {
        mViewModel.askAi(content)
    }

    private fun setAiText(content : String){
        mBinding.chatDialogAskAiResultTvReuslt.text = content
    }

    private fun initView() {
        mBinding.chatDialogAskAiResultBtnConfirm.setOnClickListener {
            dismiss()
        }
    }

    companion object{
        fun newInstance(content : String) = AskAiResultDialog().apply {
            arguments = bundleOf(
                this::mContent.name to content
            )
        }
    }
}