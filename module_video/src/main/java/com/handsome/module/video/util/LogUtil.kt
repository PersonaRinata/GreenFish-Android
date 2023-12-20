package com.handsome.module.video.util

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment


fun Fragment.makeLog(content: String) {
    Log.d("lx", "${this.javaClass.simpleName}:$content")
}

fun Activity.makeLog(content: String) {
    Log.d("lx", "${this.javaClass.simpleName}:$content")
}