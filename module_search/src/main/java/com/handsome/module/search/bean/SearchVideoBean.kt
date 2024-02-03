package com.handsome.module.search.bean

import com.handsome.api.video.bean.VideoBean
import com.handsome.lib.util.network.IApiWrapper

data class SearchVideoBean(
    override val status_code: Int,
    override val status_msg: String,
    val video_list : List<VideoBean>,
) : IApiWrapper
