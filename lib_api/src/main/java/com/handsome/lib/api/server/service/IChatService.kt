package com.handsome.lib.api.server.service

import com.alibaba.android.arouter.facade.template.IProvider

interface IChatService : IProvider {

    fun startContentListActivity(selfId : Long,otherId : Long,otherName : String)

}