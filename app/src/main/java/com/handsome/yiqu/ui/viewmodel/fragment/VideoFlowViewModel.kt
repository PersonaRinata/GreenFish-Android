package com.handsome.yiqu.ui.viewmodel.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.VideoListBean
import com.handsome.yiqu.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideoFlowViewModel : ViewModel() {

    private val _mutableLikeVideo = MutableStateFlow<VideoListBean?>(null)
    val likeVideo get() = _mutableLikeVideo.asStateFlow()

    private val _mutablePublishVide = MutableStateFlow<VideoListBean?>(null)
    val publishVideo get() = _mutablePublishVide.asStateFlow()

    fun getLikeVideo(){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            ApiService.INSTANCE.getUserLike()
        }
    }

    fun getUserPublish(){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            ApiService.INSTANCE.getUserPublish()
        }
    }

}