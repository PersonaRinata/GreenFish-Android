package com.handsome.lib.util.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

/**
 * 这个是毫秒级别的转
 */
@RequiresApi(Build.VERSION_CODES.O)
fun timeStampToTime(timestamp : Long?) : String{
    if (timestamp == null) return ""
    val instant = Instant.ofEpochSecond(timestamp / 1000000000, timestamp % 1000000000)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // 格式化日期时间
    return localDateTime.format(formatter)
}

/**
 * 这个是普通的时间戳转换
 */
fun timeToDate(timestamp: Long?): String {
    if (timestamp == null) return ""
    val date = Date(timestamp * 1000)
    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return sdf.format(date)
}

fun getCurrentTimeStamp() : Long{
    return System.currentTimeMillis() / 1000
}
