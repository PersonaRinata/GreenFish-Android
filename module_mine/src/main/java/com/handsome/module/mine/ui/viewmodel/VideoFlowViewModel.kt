package com.handsome.module.mine.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.mine.bean.VideoListBean
import com.handsome.module.mine.net.MineApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideoFlowViewModel : ViewModel() {

    private val _mutableLikeVideo = MutableStateFlow<VideoListBean?>(null)
    val likeVideo get() = _mutableLikeVideo.asStateFlow()

    private val _mutablePublishVide = MutableStateFlow<VideoListBean?>(null)
    val publishVideo get() = _mutablePublishVide.asStateFlow()

    fun getLikeVideo(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableLikeVideo.emit(MineApiService.INSTANCE.getUserLike(userId))
        }
    }

    fun getUserPublish(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutablePublishVide.emit(MineApiService.INSTANCE.getUserPublish(userId))
        }
    }

}