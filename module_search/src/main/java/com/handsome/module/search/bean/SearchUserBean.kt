package com.handsome.module.search.bean

import com.handsome.api.video.bean.AuthorBean
import com.handsome.lib.util.network.IApiWrapper

data class SearchUserBean(
    override val status_code: Int,
    override val status_msg: String,
    val user_list : List<AuthorBean>,
) : IApiWrapper
