package com.handsome.module.main.bean

import java.io.Serializable

data class VideoBean(
    val author: com.handsome.module.main.bean.AuthorBean,
    val comment_count: Int,
    val cover_url: String,
    val favorite_count: Int,
    val id: Long,
    val is_favorite: Boolean,
    val play_url: String,
    val title: String
) : Serializable