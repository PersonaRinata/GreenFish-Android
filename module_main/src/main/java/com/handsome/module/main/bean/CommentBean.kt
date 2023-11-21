package com.handsome.module.main.bean

import java.io.Serializable

data class CommentBean(
    val comment_list: List<com.handsome.module.main.bean.CommentBean.Comment>,
    val status_code: Int,
    val status_msg: String
) : Serializable {
    data class Comment(
        val content: String,
        val create_date: String,
        val id: Long,
        val user: AuthorBean
    ) : Serializable
}