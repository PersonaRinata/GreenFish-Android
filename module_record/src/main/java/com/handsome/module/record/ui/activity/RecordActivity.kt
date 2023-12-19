package com.handsome.module.record.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.record.R
import com.handsome.module.record.databinding.RecordActivityRecordBinding
import com.handsome.module.record.ui.fragment.RecordFragment

class RecordActivity : BaseActivity() {
    private val mBinding by lazy { RecordActivityRecordBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initFragment()
    }

    private fun initFragment() {
        replaceFragment(R.id.record_activity_record_frame){
            RecordFragment.newInstance()
        }
    }

    companion object{
        fun startAction(context : Context){
            val intent = Intent(context,RecordActivity::class.java)
            context.startActivity(intent)
        }
    }
}