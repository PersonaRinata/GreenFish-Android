package com.handsome.api.video.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.handsome.api.video.bean.VideoBean

interface IVideoService : IProvider{

    fun startVideoActivity(video : VideoBean)

}