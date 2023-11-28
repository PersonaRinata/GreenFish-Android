package com.handsome.module.publish.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.module.publish.bean.StatusBean
import com.handsome.module.publish.net.PublishApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.http.Part

class PublishViewModel : BaseViewModel() {

    private val _videoData = MutableSharedFlow<StatusBean>()
    val videoData : SharedFlow<StatusBean>
        get() = _videoData.asSharedFlow()

    fun uploadVideo(@Part fileBody: MultipartBody.Part,title : String) {
        viewModelScope.launch {
            _videoData.emit(PublishApiService.INSTANCE.publishVideo(fileBody, title))
        }
    }
}