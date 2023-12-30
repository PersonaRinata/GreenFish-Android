package com.handsome.module.record.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.getCurrentTimeStamp
import com.handsome.module.record.bean.IssueListBean
import com.handsome.module.record.bean.UpdateIssueListBean
import com.handsome.module.record.databinding.RecordActivityUpdateIssueListBinding
import com.handsome.module.record.ui.dialog.SaveAndBackDialog
import com.handsome.module.record.ui.viewmodel.UpdateViewModel

class UpdateIssueListActivity : BaseActivity() {
    private val mBinding by lazy { RecordActivityUpdateIssueListBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<UpdateViewModel>()
    private val mUserId by lazy { intent.getLongExtra("userId", -1) }
    private val mIssueList by lazy { intent.getSerializableExtra("IssueList") as IssueListBean.IssueList }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initView()
        initBack()
        initClick()
        initObserver()
    }

    private fun initObserver() {
        mViewModel.updateIssueList.observing {
            if (it.status_code == 0) {
                toast("信息保存成功")
            }
        }
    }

    private fun initView() {
        with(mBinding) {
            recordFragmentRecordTvSelfIntroduction.setText(mIssueList.disease_relation?.disease_introduction.toString())
            recordActivityUpdateEtGender.setText(if (mIssueList.gender) "男" else "女")
            recordActivityUpdateEtBirthday.setText(mIssueList.age.toString())
            recordActivityUpdateEtTall.setText(mIssueList.body_info?.height.toString())
            recordActivityUpdateEtWeight.setText(mIssueList.body_info?.weight.toString())
            recordActivityUpdateEtBloodSugar.setText(mIssueList.body_info?.blood_sugar.toString())
            recordActivityUpdateEtBloodPressure.setText(mIssueList.body_info?.blood_pressure.toString())
            recordActivityUpdateEtHeartRate.setText(mIssueList.body_info?.heart_rate.toString())
            recordActivityUpdateEtFamilyDisease.setText(mIssueList.disease_relation?.family_diseases.toString())
        }
    }

    private fun getIssueList(): IssueListBean.IssueList {
        with(mBinding) {
            return IssueListBean.IssueList(
                recordActivityUpdateEtBirthday.text.toString().toInt(),
                IssueListBean.IssueList.BodyInfo(
                    recordActivityUpdateEtBloodPressure.text.toString(),
                    recordActivityUpdateEtBloodSugar.text.toString(),
                    recordActivityUpdateEtHeartRate.text.toString(),
                    recordActivityUpdateEtTall.text.toString(),
                    getCurrentTimeStamp(),
                    recordActivityUpdateEtWeight.text.toString()
                ),
                IssueListBean.IssueList.DiseaseRelation(
                    recordFragmentRecordTvSelfIntroduction.text.toString(),
                    recordActivityUpdateEtFamilyDisease.text.toString(),
                    mIssueList.disease_relation?.history_diseases
                ),
                recordActivityUpdateEtGender.text.toString() == "男",
                mIssueList.username,
            )
        }
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
            mViewModel.updateIssueList(UpdateIssueListBean(getIssueList()), mUserId)
        }
    }

    // 退出但是有改变，要弹出是否退出
    private fun willBack() {
        SaveAndBackDialog(this).apply {
            setOnClickConfirm {
                dismiss()
                finishAfterTransition()
            }
            setOnClickCancel {
                dismiss()
            }
        }.show()
    }

    companion object {
        fun startAction(context: Context, issueList: IssueListBean.IssueList, userId: Long) {
            val intent = Intent(context, UpdateIssueListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("IssueList", issueList)
            intent.putExtra("userId", userId)
            context.startActivity(intent)
        }
    }
}