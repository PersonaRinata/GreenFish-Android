package com.handsome.module.publish.ui.viewmodel

import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.interceptHttpException
import com.handsome.lib.util.network.ApiStatus
import com.handsome.module.publish.net.PublishApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.http.Part

class PublishViewModel : BaseViewModel() {

    private val _videoData = MutableSharedFlow<ApiStatus>()
    val videoData : SharedFlow<ApiStatus>
        get() = _videoData.asSharedFlow()

    fun uploadVideo(@Part fileBody: MultipartBody.Part,title : String) {
        flow {
            emit(PublishApiService.INSTANCE.publishVideo(fileBody, title))
        }.interceptHttpException {}.collectLaunch {
            _videoData.emit(it)
        }
    }
}