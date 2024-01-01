package com.handsome.module.aigc.bean

data class AskAiHistoryBean(
    val msg: List<String>,
    val status_code: Int,
    val status_msg: String
)