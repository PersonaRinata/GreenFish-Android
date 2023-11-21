package com.handsome.module.main.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.main.databinding.MainActivitySearchBinding

class SearchActivity : BaseActivity() {
    private val mBinding by lazy { MainActivitySearchBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }

    companion object{
        fun startAction(context : Context){
            val intent = Intent(context,SearchActivity::class.java)
            context.startActivity(intent)
        }
    }
}