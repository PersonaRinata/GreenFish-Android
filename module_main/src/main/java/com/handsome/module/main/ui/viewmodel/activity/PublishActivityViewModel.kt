package com.handsome.module.main.ui.viewmodel.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.module.main.bean.IApiWrapper
import com.handsome.module.main.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PublishActivityViewModel : ViewModel() {

    private val _mutablePublishVideo = MutableStateFlow<IApiWrapper?>(null)
    val publishVideo = _mutablePublishVideo.asStateFlow()

    /**
     * 发布视频
     */
    fun publishVideo(video : MultipartBody.Part,title : String){
        viewModelScope.launch() {
            ApiService.INSTANCE.publishVideo(video,title)
        }
    }

}