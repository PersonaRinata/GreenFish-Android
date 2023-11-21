package com.handsome.module.main.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore


object UriToFileConverter {
    /**
     * 根据Uri获取文件的绝对路径
     *
     * @param context 上下文对象
     * @param uri  文件的Uri
     * @return 如果Uri对应的文件存在, 那么返回该文件的绝对路径, 否则返回null
     */
//    fun getRealPathFromUri(context: Context, uri: Uri): String? {
//        println("RealPathEromUri中的uri:$uri")
//        val sdkVersion = Build.VERSION.SDK_INT
//        return if (sdkVersion >= 19) { // api >= 19
//            println("sdkVersion>=19")
//            getRealPathFromUriAboveApi19(context, uri)
//        } else { // api < 19
//            getRealPathFromUriBelowAPI19(context, uri)
//        }
//    }

    open fun getRealPathFromUri(context: Context, uri: Uri): String? {
        var realPath: String? = null
        if ("content" == uri.scheme) {
            val projection = arrayOf(MediaStore.Video.Media.DATA)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                    if (columnIndex != -1) {
                        realPath = cursor.getString(columnIndex)
                    }
                }
                cursor.close()
            }
        }
        return realPath
    }


    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri  图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private fun getRealPathFromUriBelowAPI19(context: Context, uri: Uri): String? {
        return getDataColumn(context, uri, null, null)
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri  图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private fun getRealPathFromUriAboveApi19(context: Context, uri: Uri): String? {
        var filePath: String? = null
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            val documentId = DocumentsContract.getDocumentId(uri)
            if (isMediaDocument(uri)) { // MediaProvider
                println("11111111111111111111111111111111111")
                // 使用':'分割
                val id = documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1]
                println("id:$id")
                val selection = MediaStore.Video.Media._ID + "=?"
                println("selection:$selection")
                val selectionArgs = arrayOf(id)
                filePath = getDataColumn(
                    context,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    selection,
                    selectionArgs
                )
                println("filePath:$filePath")
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                println("2222222222222222222222222222222222222")
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(documentId)
                )
                filePath = getDataColumn(context, contentUri, null, null)
                println("filePath:$filePath")
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            println("333333333333333333333333333333333")
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null)
            println("filePath:$filePath")
        } else if ("file" == uri.scheme) {
            println("444444444444444444444444444444444444")
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.path
            println("filePath:$filePath")
        }
        return filePath
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(projection[0])
                path = cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            cursor?.close()
        }
        return path
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }
}
