package com.handsome.module.login.service

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.LOGIN_ENTRY
import com.handsome.lib.api.server.service.ILoginService
import com.handsome.module.login.ui.activity.LoginActivity

@Route(name = LOGIN_ENTRY,path = LOGIN_ENTRY)
class LoginService : ILoginService {

    private lateinit var mContext: Context
    override fun startLoginActivity() {
         LoginActivity.startAction(mContext)
        Log.d("lx", "startLoginActivity:跳转成功 ")
    }

    override fun init(context: Context) {
        this.mContext = context
    }
}