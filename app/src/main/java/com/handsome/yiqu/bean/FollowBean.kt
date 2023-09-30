package com.handsome.yiqu.bean

data class FollowBean(
    val status_code: Int,
    val status_msg: String,
    val user_list: List<AuthorBean>
)