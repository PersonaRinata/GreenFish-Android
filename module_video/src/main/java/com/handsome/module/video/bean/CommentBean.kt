package com.handsome.module.video.bean

import com.handsome.api.video.bean.AuthorBean
import com.handsome.lib.util.network.IApiWrapper
import java.io.Serializable

data class CommentBean(
    val comment_list: List<Comment>?,
    override val status_code: Int,
    override val status_msg: String
) : IApiWrapper {
    data class Comment(
        val content: String,
        val create_date: String,
        val id: Long,
        val user: AuthorBean
    ) : Serializable
}