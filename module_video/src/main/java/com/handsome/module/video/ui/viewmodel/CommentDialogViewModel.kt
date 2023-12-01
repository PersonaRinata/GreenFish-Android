package com.handsome.module.video.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.video.bean.CommentBean
import com.handsome.module.video.bean.StatusBean
import com.handsome.module.video.net.VideoApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommentDialogViewModel : ViewModel() {

    private val _mutableVideoComment = MutableStateFlow<CommentBean?>(null)
    val videoComment get() = _mutableVideoComment.asStateFlow()

    private val _mutableSendComment = MutableStateFlow<StatusBean?>(null)
    val sendComment get() = _mutableSendComment.asStateFlow()

    fun getVideoComment(videoId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableVideoComment.emit(VideoApiService.INSTANCE.getVideoComment(videoId))
        }
    }

    fun sendComment(videoId : Long,commentText : String){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableSendComment.emit( VideoApiService.INSTANCE.sendComment(videoId,commentText))

        }
    }

}