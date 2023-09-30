package com.handsome.yiqu.ui.viewmodel.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.FollowBean
import com.handsome.yiqu.bean.StatusBean
import com.handsome.yiqu.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FollowListViewModel : ViewModel() {

    private val _mutableFollowList = MutableStateFlow<FollowBean?>(null)
    val followList get() = _mutableFollowList.asStateFlow()

    private val _mutableFansList = MutableStateFlow<FollowBean?>(null)
    val fansList get() = _mutableFansList.asStateFlow()

    private val _mutableFollowUser = MutableStateFlow<Pair<Long,StatusBean?>?>(null)
    val followUser get() = _mutableFollowUser.asStateFlow()

    fun getFollowList(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableFollowList.emit(ApiService.INSTANCE.getFollowList(userId))
        }
    }

    fun getFansList(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableFansList.emit(ApiService.INSTANCE.getFansList(userId))
        }
    }

    fun followUser(toUserId : Long , actionId: Int){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableFollowUser.emit(toUserId to ApiService.INSTANCE.followUser(toUserId,actionId))
        }
    }
}