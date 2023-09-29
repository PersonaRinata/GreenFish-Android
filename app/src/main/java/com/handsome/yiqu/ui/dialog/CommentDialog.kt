package com.handsome.yiqu.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.getCurrentDateTime
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.CommentBean
import com.handsome.yiqu.bean.VideoBean
import com.handsome.yiqu.databinding.MainDialogCommentBinding
import com.handsome.yiqu.ui.adapter.CommentAdapter
import com.handsome.yiqu.ui.viewmodel.dialog.CommentDialogViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CommentDialog(
    private val videoBean : VideoBean,
    private val commentBean : CommentBean,
) : BottomSheetDialogFragment() {
    private val mBinding by lazy { MainDialogCommentBinding.inflate(layoutInflater) }
    private val mAdapter by lazy { CommentAdapter() }
    private val mViewModel by viewModels<CommentDialogViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObserve()
        initView()
        return mBinding.root
    }


    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch(myCoroutineExceptionHandler) {
            mViewModel.sendComment.collectLatest {
                if (it != null){
                    if (it.status_code != 0){
                        toast("发布失败")
                    }
                }
            }
        }
    }

    private fun initView() {
        mBinding.apply {
            mainDialogCommentTvCancel.setOnClickListener {
                dialog!!.cancel()
            }
            mainDialogCommentRv.apply {
                layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
                adapter = mAdapter.apply {
                    setOnClickUser {
                        //todo 跳转到他人主页
                        toast("跳转了")
                    }
                    submitList(commentBean.comment_list)
                }
            }
            mainDialogCommentTvSend.setOnClickListener {
                val commentText = mainDialogCommentEtComment.text.toString()
                // 加入候选队列
                mViewModel.sendComment(videoBean.id,commentText)
                val createDate = getCurrentDateTime()
                val comment = CommentBean.Comment(commentText,createDate,videoBean.id,videoBean.author)
                addCommentToAdapter(comment)
            }
        }
    }

    private fun addCommentToAdapter(comment: CommentBean.Comment){
        val list = mAdapter.currentList.toMutableList()
        list.add(0,comment)
        mAdapter.submitList(list)
    }

}