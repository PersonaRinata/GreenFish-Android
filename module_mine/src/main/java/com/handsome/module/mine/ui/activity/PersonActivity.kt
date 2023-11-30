package com.handsome.module.mine.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.mine.R
import com.handsome.module.mine.databinding.MineActivityPersonBinding
import com.handsome.module.mine.ui.fragment.MineFragment

class PersonActivity : BaseActivity() {
    private val mBinding by lazy { MineActivityPersonBinding.inflate(layoutInflater) }
    private var mUserId : Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mUserId = intent.getSerializableExtra("userId") as Long
        initFragment()
    }

    private fun initFragment() {
        replaceFragment(R.id.main_activity_person_container){
            MineFragment.newInstance(mUserId)
        }
    }


    companion object{
        fun startAction(context : Context,userId: Long){
            val intent = Intent(context,PersonActivity::class.java)
            intent.putExtra("userId",userId)
            context.startActivity(intent)
        }
    }

}