package com.handsome.module.video.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.search.R
import com.handsome.module.search.databinding.VideoActivityVideoBinding
import com.handsome.api.video.bean.VideoBean
import com.handsome.module.video.ui.fragment.VideoFragment

class VideoActivity : BaseActivity() {
    private val mBinding by lazy { VideoActivityVideoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val videoBean = intent.getSerializableExtra("VideoBean") as VideoBean
        replaceFragment(R.id.video_activity_video_container){
            VideoFragment.newInstance(videoBean)
        }
    }


    companion object{
        fun startAction(context : Context, videoBean : VideoBean){
            val intent = Intent(context,VideoActivity::class.java)
            intent.putExtra("VideoBean",videoBean)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }


}