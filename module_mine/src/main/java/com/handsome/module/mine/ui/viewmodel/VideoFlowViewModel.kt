package com.handsome.module.mine.ui.viewmodel

import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.interceptHttpException
import com.handsome.module.mine.bean.VideoListBean
import com.handsome.module.mine.net.MineApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class VideoFlowViewModel : BaseViewModel(){

    private val _mutableLikeVideo = MutableStateFlow<VideoListBean?>(null)
    val likeVideo get() = _mutableLikeVideo.asStateFlow()

    private val _mutablePublishVide = MutableStateFlow<VideoListBean?>(null)
    val publishVideo get() = _mutablePublishVide.asStateFlow()

    fun getLikeVideo(userId : Long){
        flow {
            emit(MineApiService.INSTANCE.getUserLike(userId))
        }.interceptHttpException {}.collectLaunch {
            _mutableLikeVideo.emit(it)
        }
    }

    fun getUserPublish(userId : Long){
        flow {
            emit(MineApiService.INSTANCE.getUserPublish(userId))
        }.interceptHttpException {}.collectLaunch {
            _mutablePublishVide.emit(it)
        }
    }

}