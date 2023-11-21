package com.handsome.module.main.bean

data class LoginBean(
    val status_code: Int,
    val status_msg: String,
    val token: String,
    val user_id: Long
)