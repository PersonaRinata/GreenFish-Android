package com.handsome.module.mine.bean

import com.handsome.api.video.bean.AuthorBean
import com.handsome.lib.util.network.IApiWrapper

data class FollowBean(
    override val status_code: Int,
    override val status_msg: String,
    val user_list: List<AuthorBean>
) : IApiWrapper