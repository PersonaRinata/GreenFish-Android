package com.handsome.lib.util.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun timeStampToTime(timestamp : Long?) : String{
    if (timestamp == null) return ""
    val instant = Instant.ofEpochSecond(timestamp / 1000000000, timestamp % 1000000000)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // 格式化日期时间
    return localDateTime.format(formatter)
}

fun getCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
}
