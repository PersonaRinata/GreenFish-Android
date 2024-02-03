package com.handsome.module.find.bean

import com.handsome.api.video.bean.VideoBean
import com.handsome.lib.util.network.IApiWrapper

data class VideoListBean(
    val next_time: Int,
    override val status_code: Int,
    override val status_msg: String,
    val video_list: List<VideoBean>?
) : IApiWrapper