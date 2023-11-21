package com.handsome.module.main.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.main.R
import com.handsome.module.main.bean.VideoBean
import com.handsome.module.main.databinding.MainActivityVideoBinding
import com.handsome.module.main.ui.fragment.VideoFragment

class VideoActivity : BaseActivity() {
    private val mBinding by lazy { MainActivityVideoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val videoBean = intent.getSerializableExtra("VideoBean") as VideoBean
        replaceFragment(R.id.main_activity_video_container){
            VideoFragment.newInstance(videoBean)
        }
    }


    companion object{
        fun startAction(context : Context, videoBean : VideoBean){
            val intent = Intent(context,VideoActivity::class.java)
            intent.putExtra("VideoBean",videoBean)
            context.startActivity(intent)
        }
    }


}