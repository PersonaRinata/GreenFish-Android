package com.handsome.yiqu.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.yiqu.R
import com.handsome.yiqu.bean.AuthorBean
import com.handsome.yiqu.databinding.MainActivityPersonBinding
import com.handsome.yiqu.ui.fragment.MineFragment

class PersonActivity : BaseActivity() {
    private val mBinding by lazy { MainActivityPersonBinding.inflate(layoutInflater) }
    private lateinit var mUserInfo : AuthorBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mUserInfo = intent.getSerializableExtra("AuthorBean") as AuthorBean
        initFragment()
    }

    private fun initFragment() {
        replaceFragment(R.id.main_activity_person_container){
            MineFragment.newInstance(mUserInfo)
        }
    }


    companion object{
        fun startAction(context : Context,authorBean: AuthorBean){
            val intent = Intent(context,PersonActivity::class.java)
            intent.putExtra("AuthorBean",authorBean)
            context.startActivity(intent)
        }
    }

}