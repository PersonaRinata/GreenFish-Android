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

    // 第二个参数传递的是当前是否喜欢这个视频，如果喜欢那么操作就是取消喜欢2。
    // 第三个参数是当前喜欢的数量
    fun updateLikeNum(videoId: Long, isFavorite: Boolean) {
        viewModelScope.launch(myCoroutineExceptionHandler) {
            // 1 是喜欢操作， 2 是不喜欢
            val actionId = if (isFavorite) 2  else  1
            // 由于结果是一样的，没有引发数据的改变，所以不会再次发射数据，又因为stateflow自己会比较新数据和旧数据的差别，所以用distinctUntilChanged没用
            val resultBean = VideoApiService.INSTANCE.updateLikeNum(videoId, actionId)
            _mutableVideoLike.emit(StatusBean(resultBean.status_code,resultBean.status_msg+System.currentTimeMillis()))
        }
    }

    fun followUser(toUserId: Long, isFollow : Boolean) {
        viewModelScope.launch(myCoroutineExceptionHandler) {
            // 1 是喜欢操作， 2 是不喜欢
            val actionId = if (isFollow) 2  else  1
            // 由于结果是一样的，没有引发数据的改变，所以不会再次发射数据，又因为stateflow自己会比较新数据和旧数据的差别，所以用distinctUntilChanged没用
            val resultBean = VideoApiService.INSTANCE.followUser(toUserId, actionId)
            _mutableFollowUser.emit(StatusBean(resultBean.status_code,resultBean.status_msg+System.currentTimeMillis()))
        }
    }

}