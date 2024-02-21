package com.handsome.lib.util.extention

import android.animation.ValueAnimator
import android.view.View

fun startValueAnimator(start: Float, end: Float, duration: Long, action: (ValueAnimator) -> Unit) : ValueAnimator{
    val animator = ValueAnimator.ofFloat(start, end)
    animator.duration = duration

    // 添加动画更新监听器，用于更新 View 的 Y 坐标
    animator.addUpdateListener { animation ->
        action.invoke(animation)
    }
    return animator
}

// 竖直方向即y轴方向平移
fun View.translateYAnimator(offsetY: Float, duration: Long) : ValueAnimator{
    val startY = this.y
    val endY = startY + offsetY
    return startValueAnimator(startY, endY, duration) { animation ->
        val value = animation.animatedValue as Float
        this.translationY = value
    }
}

// 水平方向即x轴方向平移
fun View.translateXAnimator(offsetX: Float, duration: Long) : ValueAnimator{
    val startX = this.x
    val endX = startX + offsetX
    return startValueAnimator(startX, endX, duration) { animation ->
        val value = animation.animatedValue as Float
        this.translationX = value
    }
}

// 旋转指定角度的动画
// 顺时针为正方向
fun View.rotateAnimator(changeAngle: Float, duration: Long) : ValueAnimator{
    val startAngle = this.rotation
    val endAngle = startAngle + changeAngle

    // 添加动画更新监听器，用于更新 View 的旋转角度
    return startValueAnimator(startAngle, endAngle, duration) { animation ->
    val animatedValue = animation.animatedValue as Float
        this.rotation = animatedValue
    }
}

// 透明度动画
// 1 为不透明 0为透明
fun View.alphaAnimator(changeAlpha: Float, duration: Long) : ValueAnimator{
    val startAlpha = this.alpha
    val endAlpha = startAlpha + changeAlpha

    return startValueAnimator(startAlpha, endAlpha, duration) { animation ->
        val animatedValue = animation.animatedValue as Float
        this.alpha = animatedValue
    }
}


// 尺寸动画
// 竖直方向即y轴方向平移
fun View.scaleYAnimator(scalePercent: Float, duration: Long) : ValueAnimator{
    val startY = this.width.toFloat()
    val endY = startY * scalePercent
    return startValueAnimator(startY, endY, duration) { animation ->
    val value = animation.animatedValue as Float
        this.scaleY = value
    }
}

// 水平方向即x轴方向平移
fun View.scaleXAnimator(scalePercent: Float, duration: Long) : ValueAnimator{
    val startX = this.width.toFloat()
    val endX = startX * scalePercent
    return startValueAnimator(startX, endX, duration) { animation ->
        val value = animation.animatedValue as Float
        this.scaleY = value
    }
}