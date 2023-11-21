package com.handsome.module.main.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.main.R
import com.handsome.module.main.databinding.MainActivityPersonBinding
import com.handsome.module.main.ui.fragment.MineFragment

class PersonActivity : BaseActivity() {
    private val mBinding by lazy { MainActivityPersonBinding.inflate(layoutInflater) }
    private lateinit var mUserInfo : com.handsome.module.main.bean.AuthorBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mUserInfo = intent.getSerializableExtra("AuthorBean") as com.handsome.module.main.bean.AuthorBean
        initFragment()
    }

    private fun initFragment() {
        replaceFragment(R.id.main_activity_person_container){
            MineFragment.newInstance(mUserInfo)
        }
    }


    companion object{
        fun startAction(context : Context,authorBean: com.handsome.module.main.bean.AuthorBean){
            val intent = Intent(context,PersonActivity::class.java)
            intent.putExtra("AuthorBean",authorBean)
            context.startActivity(intent)
        }
    }

}