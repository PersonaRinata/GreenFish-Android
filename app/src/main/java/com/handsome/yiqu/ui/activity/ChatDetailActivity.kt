package com.handsome.yiqu.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.yiqu.databinding.MainActivityChatDetailBinding

class ChatDetailActivity : BaseActivity(){
    private val mBinding by lazy { MainActivityChatDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }

    companion object{
        fun startAction(context : Context){
            val intent = Intent(context,ChatDetailActivity::class.java)
            context.startActivity(intent)
        }
    }
}