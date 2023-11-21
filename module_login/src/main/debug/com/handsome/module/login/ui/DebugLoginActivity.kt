package com.handsome.module.login.ui

import android.os.Bundle
import com.handsome.lib.util.base.BaseActivity
import com.handsome.module.login.ui.activity.LoginActivity

class DebugLoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoginActivity.startAction(this)
        finish()
    }
}