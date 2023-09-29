package com.handsome.lib.util.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

object MyDIffUtil{
    fun <T : Any>getNewDiff(): DiffUtil.ItemCallback<T> {
        return object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem == newItem //比较稳定值
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem == newItem //比较不稳定值，第一个方法true时
            }
        }
    }
}