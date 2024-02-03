package com.handsome.module.mine.bean

import com.handsome.lib.util.network.IApiWrapper

data class StatusBean(
    override val status_code: Int,
    override val status_msg: String
) : IApiWrapper