package com.handsome.module.chat.bean

import com.handsome.lib.util.network.IApiWrapper

data class ChatFriendsList(
    override val status_code: Int,
    override val status_msg: String,
    val user_list: List<AuthorBean>?
) : IApiWrapper