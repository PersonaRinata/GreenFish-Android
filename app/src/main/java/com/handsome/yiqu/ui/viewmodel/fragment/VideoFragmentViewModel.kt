package com.handsome.yiqu.ui.viewmodel.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.ApiWrapperUserBean
import com.handsome.yiqu.bean.CommentBean
import com.handsome.yiqu.bean.StatusBean
import com.handsome.yiqu.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideoFragmentViewModel : ViewModel() {

    private val _mutableVideoComment = MutableStateFlow<CommentBean?>(null)
    val videoComment get() = _mutableVideoComment.asStateFlow()

    private val _mutableVideoLike = MutableStateFlow<StatusBean?>(null)
    val videoLike get() = _mutableVideoLike.asStateFlow()

    private val _mutableFollowUser = MutableStateFlow<StatusBean?>(null)
    val followUser get() = _mutableFollowUser.asStateFlow()

    private val _mutableUserInfo = MutableStateFlow<ApiWrapperUserBean?>(null)
    val userInfo get() = _mutableUserInfo.asStateFlow()

    fun getVideoComment(videoId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableVideoComment.emit(ApiService.INSTANCE.getVideoComment(videoId))
        }
    }

    fun updateLikeNum(videoId : Long,actionId : Int){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableVideoLike.emit(ApiService.INSTANCE.updateLikeNum(videoId,actionId))
        }
    }

    fun followUser(toUserId : Long , actionId: Int){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableFollowUser.emit(ApiService.INSTANCE.followUser(toUserId,actionId))
        }
    }

    fun getUserInfo(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableUserInfo.emit(ApiService.INSTANCE.getUserInfo(userId))
        }
    }

}