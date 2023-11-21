package com.handsome.module.main.bean

data class FollowBean(
    val status_code: Int,
    val status_msg: String,
    val user_list: List<com.handsome.module.main.bean.AuthorBean>
)