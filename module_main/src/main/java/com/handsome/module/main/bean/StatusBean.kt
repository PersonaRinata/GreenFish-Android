package com.handsome.module.main.bean

data class StatusBean(
    override val status_code: Int,
    override val status_msg: String
) : IApiWrapper