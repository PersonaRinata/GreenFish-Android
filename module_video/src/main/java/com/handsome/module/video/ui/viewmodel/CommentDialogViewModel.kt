package com.handsome.module.video.ui.viewmodel

import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.interceptHttpException
import com.handsome.lib.util.network.ApiStatus
import com.handsome.module.video.bean.CommentBean
import com.handsome.module.video.net.VideoApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class CommentDialogViewModel : BaseViewModel() {

    private val _mutableVideoComment = MutableStateFlow<CommentBean?>(null)
    val videoComment get() = _mutableVideoComment.asStateFlow()

    private val _mutableSendComment = MutableStateFlow<ApiStatus?>(null)
    val sendComment get() = _mutableSendComment.asStateFlow()

    fun getVideoComment(videoId : Long){
        flow {
            emit(VideoApiService.INSTANCE.getVideoComment(videoId))
        }.interceptHttpException {}.collectLaunch {
            _mutableVideoComment.emit(it)
        }
    }

    fun sendComment(videoId : Long,commentText : String){
        flow {
            emit(VideoApiService.INSTANCE.sendComment(videoId,commentText))
        }.interceptHttpException {}.collectLaunch {
            _mutableSendComment.emit(it)
        }
    }

}