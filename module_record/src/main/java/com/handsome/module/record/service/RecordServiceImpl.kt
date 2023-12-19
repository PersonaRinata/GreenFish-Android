package com.handsome.module.record.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.RECORD_HEALTHY
import com.handsome.lib.api.server.service.IRecordService
import com.handsome.module.record.ui.activity.RecordActivity

@Route(name = RECORD_HEALTHY, path = RECORD_HEALTHY)
class RecordServiceImpl : IRecordService {
    private lateinit var mContext : Context
    override fun startRecordActivity() {
        RecordActivity.startAction(mContext)
    }

    override fun init(context: Context) {
        mContext = context
    }
}