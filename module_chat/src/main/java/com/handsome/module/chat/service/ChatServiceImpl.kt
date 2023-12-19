package com.handsome.module.chat.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.CHAT_CONTENT
import com.handsome.lib.api.server.service.IChatService
import com.handsome.module.chat.ui.activity.ContentListActivity

@Route(path = CHAT_CONTENT)
class ChatServiceImpl : IChatService {
    private lateinit var mContext : Context

    override fun startContentListActivity(selfId: Long, otherId: Long) {
        ContentListActivity.startAction(mContext,selfId,otherId)
    }

    override fun init(context: Context) {
        mContext = context
    }

}