package com.handsome.module.record.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.getCurrentTimeMillis
import com.handsome.module.record.bean.IssueListBean
import com.handsome.module.record.bean.UpdateIssueListBean
import com.handsome.module.record.databinding.RecordActivityUpdateIssueListBinding
import com.handsome.module.record.ui.dialog.SaveAndBackDialog
import com.handsome.module.record.ui.viewmodel.UpdateViewModel

class UpdateIssueListActivity : BaseActivity() {
    private val mBinding by lazy { RecordActivityUpdateIssueListBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<UpdateViewModel>()
    private var isChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initData()
        initView()
        initBack()
        initClick()
        initObserver()
    }

    private fun initObserver() {
        mViewModel.updateIssueList.observing {
            if (it.status_code == 0){
                toast("信息保存成功")
            }
        }
        mViewModel.issueList.observing {
            isChange = true
        }
    }

    private fun initView() {
        with(mBinding) {
            val mIssueList =  mViewModel.issueList.value
            recordFragmentRecordTvSelfIntroduction.setText(mIssueList?.disease_relation?.disease_introduction.toString())
            recordActivityUpdateEtGender.setText( mIssueList?.gender.toString())
            recordActivityUpdateEtBirthday.setText(mIssueList?.age.toString())
            recordActivityUpdateEtTall.setText( mIssueList?.body_info?.height.toString())
            recordActivityUpdateEtWeight.setText( mIssueList?.body_info?.weight.toString())
            recordActivityUpdateEtBloodSugar.setText( mIssueList?.body_info?.blood_sugar.toString())
            recordActivityUpdateEtBloodPressure.setText( mIssueList?.body_info?.blood_pressure.toString())
            recordActivityUpdateEtHeartRate.setText( mIssueList?.body_info?.heart_rate.toString())
            recordActivityUpdateEtFamilyDisease.setText( mIssueList?.disease_relation?.family_diseases.toString())
        }
    }

    private fun getIssueList() : IssueListBean.IssueList{
        with(mBinding) {
            return IssueListBean.IssueList(
                recordActivityUpdateEtBirthday.text.toString().toInt(),
                IssueListBean.IssueList.BodyInfo(
                    recordActivityUpdateEtBloodPressure.text.toString(),
                    recordActivityUpdateEtBloodSugar.text.toString(),
                    recordActivityUpdateEtHeartRate.text.toString(),
                    recordActivityUpdateEtTall.text.toString(),
                    getCurrentTimeMillis(),
                    recordActivityUpdateEtWeight.text.toString()
                ),
                IssueListBean.IssueList.DiseaseRelation(
                    recordFragmentRecordTvSelfIntroduction.text.toString(),
                    recordActivityUpdateEtFamilyDisease.text.toString(),
                    mViewModel.issueList.value?.disease_relation?.history_diseases
                ),
                recordActivityUpdateEtGender.text.toString().toBoolean(),
                mViewModel.issueList.value!!.userID,
                mViewModel.issueList.value!!.username,
            )
        }
    }

    private fun initData() {
        mViewModel.setIssueList(intent.getSerializableExtra("IssueList") as IssueListBean.IssueList)
    }

    private fun initBack() {
        val backCallBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                willBack()
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallBack)
    }

    private fun initClick() {
        mBinding.recordActivityUpdateImgBack.setOnClickListener {
            willBack()
        }
        mBinding.recordActivityRecordUpdateTvSave.setOnClickListener {
            mViewModel.updateIssueList(UpdateIssueListBean(getIssueList()))
            isChange = false
        }
    }

    // 退出但是有改变，要弹出是否退出
    private fun willBack(){
        if (isChange){
            SaveAndBackDialog(this).apply {
                setOnClickConfirm {
                    dismiss()
                    finishAfterTransition()
                }
                setOnClickCancel {
                    dismiss()
                }
            }
        }else{
            finishAfterTransition()
        }
    }

    companion object{
        fun startAction(context : Context,issueList: IssueListBean.IssueList){
            val intent = Intent(context,UpdateIssueListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("IssueList",issueList)
            context.startActivity(intent)
        }
    }
}