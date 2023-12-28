package com.handsome.module.chat.wight.selectable

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Layout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.handsome.module.chat.R
import com.handsome.module.chat.ui.dialog.AskAiResultDialog

// 封装PopupWindow的一个东西,最后松手展示的
@SuppressLint("InflateParams")
class OperateWindow(private val mTextView: TextView, private val mSelectInfo : SelectionInfo) : DoAfterSelected{
    private val mContext: Context = mTextView.context
    private var mPopupWindow: PopupWindow
    private var mTempCoors = IntArray(2)
    private var mWidth = 0
    private var mHeight = 0

    init {
        val contentView: View =
            LayoutInflater.from(mContext).inflate(R.layout.chat_popup_ask_ai, null)
        contentView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val frameLayout = contentView.findViewById<FrameLayout>(R.id.chat_popup_ask_ai_frame)
        frameLayout.setOnClickListener {
            val activity = mContext as AppCompatActivity
            AskAiResultDialog.newInstance(mSelectInfo.selectContent).show(activity.supportFragmentManager,"AskAiResultDialog")
        }
        mWidth = contentView.measuredWidth
        mHeight = contentView.measuredHeight
        mPopupWindow = PopupWindow(
            contentView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            false
        )
        mPopupWindow.isClippingEnabled = false
    }

    override fun show() {
        mTextView.getLocationInWindow(mTempCoors)
        val layout: Layout = mTextView.layout
        var posX: Int =
            layout.getPrimaryHorizontal(mSelectInfo.startPos).toInt() + mTempCoors[0] + 50
        var posY: Int =
            layout.getLineTop(layout.getLineForOffset(mSelectInfo.startPos)) + mTempCoors[1] - mHeight - 0
        if (posX <= 0) posX = 16
        if (posY < 0) posY = 16
        if (posX + mWidth > TextLayoutUtil.getScreenWidth(mContext)) {
            posX = TextLayoutUtil.getScreenWidth(mContext) - mWidth - 16
        }
        mPopupWindow.elevation = 8f
        mPopupWindow.showAtLocation(mTextView, Gravity.NO_GRAVITY, posX, posY)
    }

    override fun dismiss() {
        mPopupWindow.dismiss()
    }

}