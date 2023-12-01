package com.handsome.module.find.bean

import com.handsome.api.video.bean.VideoBean
import java.io.Serializable

data class VideoListBean(
    val next_time: Int,
    val status_code: Int,
    val status_msg: String,
    val video_list: List<VideoBean>
) : Serializable