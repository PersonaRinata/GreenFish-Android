package com.handsome.module.mine.bean

import java.io.Serializable

data class FollowBean(
    val status_code: Int,
    val status_msg: String,
    val user_list: List<AuthorBean>
) : Serializable