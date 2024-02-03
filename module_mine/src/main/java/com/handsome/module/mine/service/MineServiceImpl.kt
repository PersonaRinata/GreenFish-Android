package com.handsome.module.mine.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.PERSON_ENTRY
import com.handsome.lib.api.server.service.IMineService
import com.handsome.module.mine.ui.activity.PersonActivity

@Route(path = PERSON_ENTRY)
class MineServiceImpl : IMineService {
    private lateinit var mContext: Context

    override fun startPersonActivity(userId: Long) {
        PersonActivity.startAction(mContext,userId)
    }


    override fun init(context: Context) {
        mContext = context
    }
}