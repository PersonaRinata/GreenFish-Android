package com.handsome.module.chat.wight.selectable

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.Spannable
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.ColorInt
import com.handsome.module.chat.R
import com.handsome.module.chat.wight.selectable.TextLayoutUtil.getPreciseOffset

/**
 * 通过自定义view实现选中
 * 只管滑动的时候隐藏，不管显示
 * 但是暴漏了show方法和hide方法，可以自由决定显示还是隐藏
 */
@SuppressLint("ClickableViewAccessibility")
class SelectableTextViewHelper(private val mTextView: TextView) {
    // 初始配置信息
    private var mOnScrollChangedListener: OnScrollChangedListener
    private var mSelectTextCallBack: ((String) -> Unit)? = null
    private var mCursorColor = R.color.chat_cursor_color
    private var mSelectBgColor = R.color.chat_select_bg
    private var mCursorSizeDp = 30

    // 需要的位置信息
    private var mTouchX: Float = -1f
    private var mTouchY: Float = -1f
    private var isHide: Boolean = true

    private val mContext = mTextView.context

    // 选中的信息，选中起始位置，选中终点位置，选中文字，为了保持数据的一致性
    private val mSelectInfo by lazy { SelectionInfo() }

    // 图标等布局信息
    private val mWindow: DoAfterSelected by lazy { OperateWindow(mTextView,mSelectInfo) }
    private val mStartCursor: MyCursor by lazy { MyCursor(true) }
    private val mEndCursor: MyCursor by lazy { MyCursor(false) }
    private val mSpannable: Spannable by lazy { mTextView.text as Spannable }
    private val mSpan: BackgroundColorSpan by lazy { BackgroundColorSpan(mContext.resources.getColor(mSelectBgColor, null)) }

    init {
        // 初始化一些基本操作
        // 让textView变成 可以 多种样式结合的
        mTextView.setText(mTextView.text, TextView.BufferType.SPANNABLE)
        mTextView.setOnLongClickListener {
            showSelectView()
            return@setOnLongClickListener true
        }
        mTextView.setOnTouchListener { _, event ->
            mTouchX = event.x
            mTouchY = event.y
            return@setOnTouchListener false
        }
        mTextView.setOnClickListener {
            resetSelectionInfo()
            hideSelectView()
        }
        mTextView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}
            override fun onViewDetachedFromWindow(v: View) {
                destroy()
            }
        })

        mOnScrollChangedListener = OnScrollChangedListener {
            if (!isHide){
                hideSelectView()
            }
        }
        mTextView.viewTreeObserver.addOnScrollChangedListener(mOnScrollChangedListener)
    }

    /**
     * 长按进入
     * 全部选中，展示光标，展示全选背景
     * 具体拖动,window隐藏，跟着手指拖动
     */
    private fun showSelectView() {
        // 隐藏上次拖动的结果
        hideSelectView()
        resetSelectionInfo()

        // 选中全部，主流软件设置
        selectText(0, mTextView.text.length)

        showAll()
    }

    fun showAll(){
        // 显示光标和窗口
        isHide = false
        showCursor(mStartCursor)
        showCursor(mEndCursor)
        mWindow.show()
    }


    /**
     * 将起点位置到终点位置的文本选中
     */
    private fun selectText(startPos: Int, endPos: Int) {
        // 更新最新的位置
        if (startPos != -1) {
            mSelectInfo.startPos = startPos
        }
        if (endPos != -1) {
            mSelectInfo.endPos = endPos
        }

        // 前面移动到后面，交换位置
        if (mSelectInfo.startPos > mSelectInfo.endPos) {
            val temp: Int = mSelectInfo.startPos
            mSelectInfo.startPos = mSelectInfo.endPos
            mSelectInfo.endPos = temp
        }

        mSelectInfo.selectContent =
            mSpannable.subSequence(mSelectInfo.startPos, mSelectInfo.endPos).toString()
        mSpannable.setSpan(
            mSpan,
            mSelectInfo.startPos,
            mSelectInfo.endPos,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        mSelectTextCallBack?.invoke(mSelectInfo.selectContent)
    }

    private fun showCursor(cursor: MyCursor) {
        val layout = mTextView.layout
        val offset: Int = if (cursor.isLeft) mSelectInfo.startPos else mSelectInfo.endPos
        cursor.show(
            layout.getPrimaryHorizontal(offset).toInt(),
            layout.getLineBottom(layout.getLineForOffset(offset))
        )
    }

    fun hideSelectView() {
        if (isHide) return
        isHide = true
        mStartCursor.dismiss()
        mEndCursor.dismiss()
        mSpannable.removeSpan(mSpan)
        mWindow.dismiss()
    }

    private fun resetSelectionInfo() {
        mSelectInfo.startPos = -1
        mSelectInfo.endPos = -1
        mSelectInfo.selectContent = ""
    }

    fun destroy() {
        mTextView.viewTreeObserver.removeOnScrollChangedListener(mOnScrollChangedListener)
        resetSelectionInfo()
        hideSelectView()
        val result =  mTextView.rootView as ViewGroup?
        result?.removeView(mStartCursor)
        result?.removeView(mEndCursor)
    }

    private fun getCursorHandle(isLeft: Boolean): MyCursor {
        return if (mStartCursor.isLeft == isLeft) mStartCursor else mEndCursor
    }


    @SuppressLint("ResourceAsColor")
    private inner class MyCursor(var isLeft: Boolean) : View(mContext) {
        private val mPopupWindow: PopupWindow
        private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val mCircleRadius: Int = mCursorSizeDp / 2
        private val mWidth = mCircleRadius * 2
        private val mHeight = mCircleRadius * 2
        private val mPadding = 0

        init {
            mPaint.color = mCursorColor
            mPopupWindow = PopupWindow(this)
            mPopupWindow.isClippingEnabled = false
            mPopupWindow.width = mWidth + mPadding * 2
            mPopupWindow.height = mHeight + mPadding / 2
            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawCircle(
                (mCircleRadius + mPadding).toFloat(),
                mCircleRadius.toFloat(),
                mCircleRadius.toFloat(),
                mPaint
            )
        }

        private var mBeforeDragStart = 0
        private var mBeforeDragEnd = 0
        override fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mBeforeDragStart = mSelectInfo.startPos
                    mBeforeDragEnd = mSelectInfo.endPos
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mWindow.show()
                MotionEvent.ACTION_MOVE -> {
                    mWindow.dismiss()
                    val rawX = event.rawX.toInt()
                    val rawY = event.rawY.toInt()
                    update(rawX, rawY)
                }
            }
            return true
        }

        private fun changeDirection() {
            isLeft = !isLeft
            invalidate()
        }

        fun dismiss() {
            mPopupWindow.dismiss()
        }

        private val mTempCoors = IntArray(2)

        fun update(absoluteX : Int, absoluteY: Int) {
            var y = absoluteY
            mTextView.getLocationInWindow(mTempCoors)
            val oldOffset: Int = if (isLeft) {
                mSelectInfo.startPos
            } else {
                mSelectInfo.endPos
            }
            y -= mTempCoors[1]
            val newOffset = getPreciseOffset(mTextView,absoluteX - mTempCoors[0].toFloat(),absoluteY - mTempCoors[1].toFloat())
            if (newOffset != oldOffset) {
                resetSelectionInfo()
                if (isLeft) {
                    if (newOffset > mBeforeDragEnd) {
                        val handle = getCursorHandle(false)
                        changeDirection()
                        handle.changeDirection()
                        mBeforeDragStart = mBeforeDragEnd
                        selectText(mBeforeDragEnd, newOffset)
                        handle.updateCursor()
                    } else {
                        selectText(newOffset, mBeforeDragEnd)
                    }
                    updateCursor()
                } else {
                    if (newOffset < mBeforeDragStart) {
                        val handle = getCursorHandle(true)
                        handle.changeDirection()
                        changeDirection()
                        mBeforeDragEnd = mBeforeDragStart
                        selectText(newOffset, mBeforeDragStart)
                        handle.updateCursor()
                    } else {
                        selectText(mBeforeDragStart, newOffset)
                    }
                    updateCursor()
                }
            }
        }

        private fun updateCursor() {
            mTextView.getLocationInWindow(mTempCoors)
            val layout: Layout = mTextView.layout
            if (isLeft) {
                mPopupWindow.update(
                    layout.getPrimaryHorizontal(mSelectInfo.startPos).toInt() - mWidth + extraX,
                    layout.getLineBottom(layout.getLineForOffset(mSelectInfo.startPos)) + extraY,
                    -1,
                    -1
                )
            } else {
                mPopupWindow.update(
                    layout.getPrimaryHorizontal(mSelectInfo.endPos).toInt() + extraX,
                    layout.getLineBottom(layout.getLineForOffset(mSelectInfo.endPos)) + extraY,
                    -1,
                    -1
                )
            }
        }

        fun show(x: Int, y: Int) {
            mTextView.getLocationInWindow(mTempCoors)
            val offset = if (isLeft) mWidth else 0
            mPopupWindow.showAtLocation(
                mTextView,
                Gravity.NO_GRAVITY,
                x - offset + extraX,
                y + extraY
            )
        }

        val extraX: Int
            get() = mTempCoors[0] - mPadding + mTextView.paddingLeft
        val extraY: Int
            get() = mTempCoors[1] + mTextView.paddingTop
    }

    fun setCursorColor(@ColorInt cursorColor: Int) {
        this.mCursorColor = cursorColor
    }

    fun setSelectBgColor(@ColorInt selectBgColor: Int) {
        this.mSelectBgColor = selectBgColor
    }

    fun setCursorSizeDp(cursorSizeDp: Int) {
        this.mCursorSizeDp = cursorSizeDp
    }

    // 这个在当前app中用不到
    fun setSelectTextCallBack(selectTextCallBack: (String) -> Unit) {
        this.mSelectTextCallBack = selectTextCallBack
    }
}