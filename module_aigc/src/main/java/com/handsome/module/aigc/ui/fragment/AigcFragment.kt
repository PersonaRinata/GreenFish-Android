package com.handsome.module.aigc.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.MAIN_AIGC
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.api.server.service.IChatService
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.service.impl
import com.handsome.module.aigc.databinding.AigcFragmentAigcBinding
import com.handsome.module.aigc.ui.adapter.MessageAdapter
import com.handsome.module.aigc.ui.viewmodel.AigcViewModel

@Route(name = MAIN_AIGC, path = MAIN_AIGC)
class AigcFragment : BaseFragment() {
    private val mBinding by lazy { AigcFragmentAigcBinding.inflate(layoutInflater) }
    private val mUserInfo by lazy { IAccountService::class.impl.getUserInfo() }
    private val mViewModel by viewModels<AigcViewModel>()
    private val mAdapter by lazy { MessageAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initClick()
        initRv()
        initKeyBoard()
        initObserve()
        getHistoryData()
        return mBinding.root
    }

    private fun initKeyBoard() {
//        // 告诉系统不要自动调整布局以适应系统窗口装饰
//        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
//        mBinding.root.viewTreeObserver.addOnGlobalLayoutListener {
//            val rootHeight = mBinding.root.height
//            val rect = Rect()
//            mBinding.root.getWindowVisibleDisplayFrame(rect)
//            val keyBoardHeight = rootHeight - rect.bottom
//        }
    }

    private fun initRv() {
        with(mBinding.aigcFragmentAigcRvMessage) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter.apply {
                setOnClickDoctor {
                    if (mUserInfo != null) {
                        IChatService::class.impl.startContentListActivity(
                            mUserInfo!!.user_id,
                            it.id,
                            it.name
                        )
                    } else {
                        toast("登录过期，请重新登录~")
                    }
                }
            }
        }
    }

    private fun initClick() {
        mBinding.aigcFragmentAigcTvRecommendDoc.setOnClickListener {
            val text = getText()
            if (text.trim() != "") {
                recommendDoctor(text)
            } else {
                toast("输入不能为空哦~")
            }
        }
        mBinding.aigcFragmentAigcTvSend.setOnClickListener {
            val text = getText()
            if (text.trim() != "") {
                askAiQuestion(text)
            } else {
                toast("输入不能为空哦~")
            }
        }
    }

    private fun getHistoryData() {
        mViewModel.aigcHistory()
    }

    private fun askAiQuestion(content: String) {
        mViewModel.askQuestion(content)
        addHistoryToAdapter(listOf(content))
    }

    private fun recommendDoctor(content: String) {
        mViewModel.recommendDoctor(content)
    }

    private fun scrollToRvEnd() {
        with(mBinding.aigcFragmentAigcRvMessage) {
            post {
                smoothScrollToPosition(computeVerticalScrollRange())
            }
        }
    }

    private fun getText(): String {
        val text = mBinding.aigcFragmentAigcEt.text.toString()
        mBinding.aigcFragmentAigcEt.setText("")
        return text
    }

    private fun addHistoryToAdapter(data: List<String>) {
        val messages = ArrayList<MessageAdapter.ContentListData.TypeLeftRight>()
        data.forEach {
            messages.add(MessageAdapter.ContentListData.TypeLeftRight(it))
        }
        addItemsToAdapter(messages)
    }

    private fun addItemsToAdapter(data: List<MessageAdapter.ContentListData>) {
        val list = mAdapter.currentList.toMutableList()
        list.addAll(data)
        mAdapter.submitList(list)
    }

    private fun initObserve() {
        mViewModel.aigcHistory.observing {
            if (it.isSuccess(requireActivity())) {
                addHistoryToAdapter(it.msg)
                scrollToRvEnd()
            } else {
                toast("网络异常，请重试~")
            }
        }
        mViewModel.askQuestion.observing {
            if (it.isSuccess(requireActivity())) {
                addHistoryToAdapter(listOf(it.msg))
                scrollToRvEnd()
            } else {
                toast("网络异常，请重试~")
            }
        }
        mViewModel.recommendDoctor.observing {
            if (it.second.isSuccess(requireActivity())) {
                addItemsToAdapter(listOf(MessageAdapter.ContentListData.TypeRecommendDoctor(it.first to it.second.doctor_list)))
                scrollToRvEnd()
            } else {
                toast("网络异常，请重试~")
            }
        }
    }
}