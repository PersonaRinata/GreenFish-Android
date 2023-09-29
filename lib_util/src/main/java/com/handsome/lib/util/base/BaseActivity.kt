package com.handsome.lib.util.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle

abstract class BaseActivity : AppCompatActivity() {

    /**
     * 是否处于转屏或异常重建后的 Activity 状态
     */
    protected var mIsActivityRebuilt = false
        private set

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        mIsActivityRebuilt = savedInstanceState != null
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initDependStatusBar()

    }

    private fun initDependStatusBar() {
        val window = this.window
        val decorView = window.decorView

        // 这是 Android 做了兼容的 Compat 包
        // 注意，使用了下面这个方法后，状态栏不会再有东西占位，
        // 可以给根布局加上 android:fitsSystemWindows=true
        // 不同布局该属性效果不同，请给合适的布局添加
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, decorView)
        // 如果你要白色的状态栏字体，请在你直接的 Activity 中单独设置 isAppearanceLightStatusBars，这里不提供方法
        windowInsetsController.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.TRANSPARENT //把状态栏颜色设置成透明
    }

    /**
     * 替换 Fragment 的正确用法。
     * 如果不按照正确方式使用，会造成 ViewModel 失效，
     * 你可以写个 demo 看看在屏幕翻转后 Fragment 的 ViewModel 的 hashcode() 值是不是同一个
     *
     * 没有返回 Fragment 的原因：Fragment 一般是不能直接暴露方法让外面调用的，所以拿了作用不大，
     * 想宿主给子 Fragment 通信，请使用 ViewModel。
     * 但返过来通信时，是允许直接强转的（requestFragment() as XXXFragment）
     */
    protected fun <F : Fragment> replaceFragment(
        @IdRes id: Int,
        func: FragmentTransaction.() -> F
    ) {
        if (lifecycle.currentState == Lifecycle.State.CREATED) {
            // 处于 onCreate 时
            if (mIsActivityRebuilt) {
                // 如果此时 Activity 处于重建状态，Fragment 会自动恢复，不能重复提交而改变之前的状态
                // 因为存在重建前你在 onCreate 中提交的 Fragment 在后面因为其他事件取消了，
                // 但如果你在这里进行提交，就会导致本来被取消了的 界面 重新出现
            } else {
                // Activity 正常被创建，即没有被异常摧毁
                supportFragmentManager.beginTransaction()
                    .apply { replace(id, func.invoke(this)) }
                    .commit()
            }
        } else {
            // 除了 onCreate 外的其他生命周期，直接提交即可，一般也遇不到在 onStart 等生命周期中提交 Fragment
            // 如果你要判断是否重复提交同类型的 Fragment，这是不好判断的，因为 reified 关键字如果匹配到 超类 Fragment 就会导致判断错误
            supportFragmentManager.beginTransaction()
                .apply { replace(id, func.invoke(this)) }
                .commit()
        }
    }
}