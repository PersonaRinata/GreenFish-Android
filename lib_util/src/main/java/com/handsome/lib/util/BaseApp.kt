package com.handsome.lib.util

import android.app.Application
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2023649401@qq.com
 * @date 2023/7/16
 * @Description:
 */
open class BaseApp : Application() {
    companion object {
        lateinit var mContext: BaseApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        initARouter()
    }
    /**
     * 在单模块调试时也需要该 ARouter，所以直接在这里初始化
     */
    private fun initARouter() {
        val isDebugARouter = true
        if (isDebugARouter) {
            //下面两行必须写在init之前，否则这些配置在init中将无效
            ARouter.openLog();
            //开启调试模式（如果在InstantRun模式下运行，必须开启调试模式！
            // 线上版本需要关闭，否则有安全风险）
            ARouter.openDebug();
        }
        ARouter.init(this)
        Log.d("lx", "initARouter: 加载成功")
    }


}
