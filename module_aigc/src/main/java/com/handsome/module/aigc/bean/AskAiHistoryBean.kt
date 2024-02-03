package com.handsome.module.aigc.bean

import com.handsome.lib.util.network.IApiWrapper

data class AskAiHistoryBean(
    val msg: List<String>,
    override val status_code: Int,
    override val status_msg: String
) : IApiWrapper