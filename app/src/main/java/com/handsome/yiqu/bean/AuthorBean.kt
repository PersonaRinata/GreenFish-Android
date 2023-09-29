package com.handsome.yiqu.bean

import java.io.Serializable

data class AuthorBean(
    override val status_code: Int,
    override val status_msg: String,
    val avatar: String?,
    val background_image: String,
    val favorite_count: Int,
    val follow_count: Int,
    val follower_count: Int,
    val id: Long,
    val is_follow: Boolean,
    val name: String?,
    val signature: String?,
    val total_favorited: Int,
    val work_count: Int
) : Serializable , IApiWrapper

data class ApiWrapperUserBean(
    val user: AuthorBean,
    override val status_code: Int,
    override val status_msg: String,
) : IApiWrapper ,Serializable