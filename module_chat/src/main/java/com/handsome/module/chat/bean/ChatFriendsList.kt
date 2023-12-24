package com.handsome.module.chat.bean

import java.io.Serializable

data class ChatFriendsList(
    val status_code: Int,
    val status_msg: String,
    val user_list: List<AuthorBean>?
) : Serializable