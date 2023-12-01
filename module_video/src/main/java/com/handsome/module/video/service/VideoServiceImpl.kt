package com.handsome.module.video.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.api.video.VIDEO_ENTRY
import com.handsome.api.video.service.IVideoService
import com.handsome.module.video.ui.activity.VideoActivity

@Route(name = VIDEO_ENTRY,path = VIDEO_ENTRY)
class VideoServiceImpl : IVideoService {
    private lateinit var mContext : Context

    override fun startVideoActivity(videoBean: com.handsome.api.video.bean.VideoBean) {
        VideoActivity.startAction(mContext,videoBean)
    }

    override fun init(context: Context) {
        mContext = context
    }
}