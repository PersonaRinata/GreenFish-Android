package com.handsome.module.main.service


import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.MAIN_MAIN
import com.handsome.lib.api.server.service.IMainService
import com.handsome.module.main.MainActivity

@Route(path = MAIN_MAIN)
class MainService : IMainService {
    private lateinit var mContext : Context

    override fun startMainActivity() {
        MainActivity.startAction(mContext)
    }

    override fun init(context: Context) {
        this.mContext = context
    }
}