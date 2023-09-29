package com.handsome.lib.util.util

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import com.handsome.lib.util.extention.toast

val myCoroutineExceptionHandler = CoroutineExceptionHandler{ _,e ->
    Log.d("lx", "net error: $e")
    e.toString().toast()
}