package com.handsome.module.app

import android.os.Bundle
import android.view.MotionEvent
import com.handsome.lib.api.server.service.IMainService
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.service.impl

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_main)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when(event.action){
            MotionEvent.ACTION_DOWN -> {
                IMainService::class.impl.startMainActivity()
                finish()
                true
            }
            else -> {
                super.onTouchEvent(event)
            }
        }
    }
}