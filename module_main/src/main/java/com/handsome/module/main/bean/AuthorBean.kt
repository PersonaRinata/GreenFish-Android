package com.handsome.module.main.bean

import java.io.Serializable

data class AuthorBean(
    override val status_code: Int,
    override val status_msg: String,
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
    val work_count: Int
) : Serializable , com.handsome.module.main.bean.IApiWrapper

data class ApiWrapperUserBean(
    val user: com.handsome.module.main.bean.AuthorBean,
    override val status_code: Int,
    override val status_msg: String,
) : com.handsome.module.main.bean.IApiWrapper,Serializable