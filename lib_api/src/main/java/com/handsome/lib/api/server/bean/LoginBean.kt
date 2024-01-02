package com.handsome.lib.api.server.bean

import java.io.Serializable

data class LoginBean(
    val status_code: Int,
    val status_msg: String,
    val token: String,
    val user_id: Long,
    val username : String,
    val password : String
) : Serializable