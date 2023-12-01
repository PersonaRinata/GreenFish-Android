package com.handsome.module.mine.bean

import com.handsome.api.video.bean.AuthorBean
import java.io.Serializable

data class FollowBean(
    val status_code: Int,
    val status_msg: String,
    val user_list: List<AuthorBean>
) : Serializable