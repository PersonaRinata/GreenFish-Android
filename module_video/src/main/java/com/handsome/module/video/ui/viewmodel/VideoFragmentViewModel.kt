package com.handsome.module.video.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.video.bean.StatusBean
import com.handsome.module.video.net.VideoApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideoFragmentViewModel : ViewModel() {

    private val _mutableVideoLike = MutableStateFlow<StatusBean?>(null)
    val videoLike get() = _mutableVideoLike.asStateFlow()

    private val _mutableFollowUser = MutableStateFlow<StatusBean?>(null)
    val followUser get() = _mutableFollowUser.asStateFlow()


    fun updateLikeNum(videoId : Long,actionId : Int){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableVideoLike.emit(VideoApiService.INSTANCE.updateLikeNum(videoId,actionId))
        }
    }

    fun followUser(toUserId : Long , actionId: Int){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableFollowUser.emit(VideoApiService.INSTANCE.followUser(toUserId,actionId))
        }
    }

}