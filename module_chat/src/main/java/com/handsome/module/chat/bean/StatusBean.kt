package com.handsome.module.chat.bean

import java.io.Serializable

data class StatusBean(
    val status_code: Int,
    val status_msg: String
) : Serializable