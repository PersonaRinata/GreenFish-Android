package com.handsome.module.record.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.MAIN_RECORD
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.service.impl
import com.handsome.lib.util.util.timeToDate
import com.handsome.module.record.bean.IssueListBean
import com.handsome.module.record.databinding.RecordFramentRecordBinding
import com.handsome.module.record.ui.activity.UpdateIssueListActivity
import com.handsome.module.record.ui.adapter.DiseaseHistoryAdapter
import com.handsome.module.record.ui.viewmodel.RecordFragmentViewModel

@Route(name = MAIN_RECORD, path = MAIN_RECORD)
class RecordFragment : BaseFragment() {
    private val mBinding by lazy { RecordFramentRecordBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<RecordFragmentViewModel>()
    private val mUserInfo by lazy { IAccountService::class.impl.getUserInfo() }
    private val mAdapter by lazy { DiseaseHistoryAdapter() }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initRv()
        initClick()
        initObserver()
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        mUserInfo?.let { getData(it.user_id) }
    }

    private fun initClick() {
        mBinding.recordFragmentRecordTvUpdate.setOnClickListener {
            // 跳转到记录界面
            if (mViewModel.issueList.value != null){
                UpdateIssueListActivity.startAction(requireContext(),mViewModel.issueList.value!!.issue_list,mUserInfo!!.user_id)
            }else{
                toast("网络异常，请重试")
            }
        }
    }

    private fun getData(userId : Long) {
        mViewModel.getIssueList(userId)
        mViewModel.getUserInfo(userId)
    }

    private fun initRv() {
        with(mBinding.recordFragmentRecordRvDiseaseHistory) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setBodyInfo(userInfo : IssueListBean.IssueList){
        with(mBinding) {
            recordFragmentPersonalizationTvUsername.text = userInfo.username
            recordFragmentPersonalizationTvGender.text = if(userInfo.gender) "男" else "女"
            val age = "${userInfo.age}岁"
            recordFragmentPersonalizationTvAge.text = age

            val bodyInfo =  userInfo.body_info
            bodyInfo?.let {
                val updateTime = timeToDate(it.update_time)
                recordFragmentRecordTvTallValue.text = it.height
                recordFragmentRecordTvWeightValue.text = it.weight
                recordFragmentRecordTvSugarValue.text = it.blood_sugar
                recordFragmentRecordTvSugarUpdateTime.text = updateTime
                recordFragmentRecordTvBloodPressureValue.text = it.blood_pressure
                recordFragmentRecordTvBloodPressureTime.text = updateTime
                recordFragmentRecordTvHeartRateValue.text = it.heart_rate
                recordFragmentRecordTvHeartRateUpdateTime.text = updateTime
                recordFragmentRecordTvFamilyDiseaseUpdateTime.text = updateTime
            }
            val diseaseInfo = userInfo.disease_relation
            diseaseInfo?.let {
                recordFragmentRecordTvFamilyDiseaseValue.text = it.family_diseases
                recordFragmentRecordTvSelfIntroduction.text = it.disease_introduction
                mAdapter.submitList(it.history_diseases)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initObserver() {
        mViewModel.issueList.observing{
            if (it.isSuccess(requireActivity())){
                setBodyInfo(it.issue_list)
            }else{
                toast("网络异常，请重试~")
            }
        }
        mViewModel.userInfo.observing {
            if (it.isSuccess(requireActivity())){
                if (it.user.avatar != "") mBinding.recordFragmentPersonalizationImgUser.setImageFromUrl(it.user.avatar)
            }else{
                toast("网络异常，请重试~")
            }
        }
    }

    companion object{
        fun newInstance() = RecordFragment()
    }
}