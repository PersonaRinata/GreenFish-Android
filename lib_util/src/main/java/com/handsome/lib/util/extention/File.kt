package com.handsome.lib.util.extention

import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.handsome.lib.util.BaseApp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


val File.uri: Uri
    get() = if (Build.VERSION.SDK_INT >= 24) {
        FileProvider.getUriForFile(BaseApp.mContext, "${BaseApp.mContext.packageName}.fileProvider", this)
    } else {
        Uri.fromFile(this)
    }

fun File.getRequestBody(): RequestBody {
    return this.asRequestBody("multipart/form-data".toMediaTypeOrNull())
}