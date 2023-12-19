package com.handsome.module.search.bean

import com.handsome.api.video.bean.VideoBean

data class SearchVideoBean(
    val status_code: Int,
    val status_msg: String,
    val video_list : List<VideoBean>,
)
