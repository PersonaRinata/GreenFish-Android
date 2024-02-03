package com.handsome.lib.api.server.service

import com.alibaba.android.arouter.facade.template.IProvider

interface IMineService : IProvider {

    fun startPersonActivity(userId: Long)

}