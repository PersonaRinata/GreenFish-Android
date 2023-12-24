package com.handsome.module.chat.wight.selectable

import android.content.Context
import android.widget.TextView

object TextLayoutUtil {

    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getPreciseOffset(textView: TextView, x: Float, y: Float): Int {
        val layout = textView.layout ?: return -1

        val topVisibleLine = layout.getLineForVertical(y.toInt())
        val offset = layout.getOffsetForHorizontal(topVisibleLine, x)
        val offsetX = layout.getPrimaryHorizontal(offset)

        return if (offsetX > x) {
            layout.getOffsetToLeftOf(offset)
        } else {
            offset
        }
    }
}
