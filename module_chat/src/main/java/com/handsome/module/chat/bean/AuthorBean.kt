package com.handsome.module.chat.bean

import java.io.Serializable

data class AuthorBean(
    val status_code: Int,
    val status_msg: String,
    val avatar: String,
    val background_image: String,
    val favorite_count: Int,
    val follow_count: Int,
    val follower_count: Int,
    val id: Long,
    val is_follow: Boolean,
    val message: String,
    val msgType: Int,
    val name: String,
    val signature: String,
    val total_favorited: Int,
    val work_count: Int,
    var isTop : Boolean,
    var isOpen : Boolean
) : Serializable