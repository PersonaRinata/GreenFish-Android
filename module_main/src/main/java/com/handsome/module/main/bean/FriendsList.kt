package com.handsome.module.main.bean

import java.io.Serializable

data class FriendsList(
    val status_code: Int,
    val status_msg: String,
    val user_list: List<com.handsome.module.main.bean.AuthorBean>
) : Serializable