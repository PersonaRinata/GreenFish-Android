package com.handsome.module.publish.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.MAIN_PUBLISH
import com.handsome.lib.api.server.service.IPublishService
import com.handsome.module.publish.ui.activity.PublishActivity

@Route(name = MAIN_PUBLISH, path = MAIN_PUBLISH)
class PublishServiceImpl : IPublishService {
    private lateinit var mContext: Context

    override fun startPublishActivity() {
        PublishActivity.startAction(mContext)
    }

    override fun init(context: Context) {
        mContext = context
    }
}