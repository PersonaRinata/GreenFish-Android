package com.handsome.lib.util.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * 获得当前时间戳
 */
fun getCurrentTime(): Long {
    return System.currentTimeMillis()
}

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val currentDate = Date()
    return sdf.format(currentDate)
}
