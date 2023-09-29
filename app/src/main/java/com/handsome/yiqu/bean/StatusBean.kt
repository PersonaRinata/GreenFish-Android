package com.handsome.yiqu.bean

data class StatusBean(
    override val status_code: Int,
    override val status_msg: String
) : ApiWrapper