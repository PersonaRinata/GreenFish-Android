package com.handsome.module.video.wight

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.handsome.module.search.R

// 一个ViewGroup，一个爱心的ImageView
// 重写事件分发
// 双击就出来爱心
// 先来动画，缩放动画，位移动画，透明动画
class LoveViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val gestureDetector by lazy {
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                // 在此处处理双击事件
                // 双击就点赞
                mOnDoubleTap?.invoke()
                show(e.x, e.y)
                return true
            }

        })
    }

    private var mOnDoubleTap : (() -> Unit)? = null

    fun setOnDoubleTap(onDoubleTap : () -> Unit){
        this.mOnDoubleTap = onDoubleTap
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (gestureDetector.onTouchEvent(ev)) {
            true
        } else {
            super.onInterceptTouchEvent(ev)
        }
    }

    private fun show(x: Float, y: Float) {
        val imageView = ImageView(context)
        val width = 300
        val height = 300
        imageView.layoutParams = LayoutParams(width, height).apply {
            leftMargin = (x - width / 2).toInt()
            topMargin = (y - height / 2).toInt()
        }
        imageView.setImageResource(R.drawable.video_ic_like)
        addView(imageView)
        val set = getAnimatorSet(imageView)
        set.start()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                removeViewInLayout(imageView)
            }
        })
    }

    private fun getAnimatorSet(view: View): AnimatorSet {
        val set = AnimatorSet()
        //放大
        set.playSequentially(
            AnimatorSet().apply {
                // 同时播放
                playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f).apply {
                        duration = 50
                    },
                    ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f).apply {
                        duration = 50
                    },
                    ObjectAnimator.ofFloat(view, "rotation", -20f, -6f).apply {
                        duration = 50
                    }
                )
            },
            //缩小
            AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1f).apply {
                        duration = 200
                    },
                    ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1f).apply {
                        duration = 200
                    },
                    ObjectAnimator.ofFloat(view, "rotation", -6f, 4f, 0f).apply {
                        duration = 200
                    }
                )
            },
            ValueAnimator.ofInt(0, 100).apply {
                duration = 200
            },
            //缩放/透明并位移
            AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.8f).apply {
                        duration = 500
                    },
                    ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.8f).apply {
                        duration = 500
                    },
                    ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).apply {
                        duration = 500
                    }
                )
            })
        return set
    }
}