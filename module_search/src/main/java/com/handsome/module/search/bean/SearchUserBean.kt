package com.handsome.module.search.bean

import com.handsome.api.video.bean.AuthorBean

data class SearchUserBean(
    val status_code: Int,
    val status_msg: String,
    val user_list : List<AuthorBean>,
)
