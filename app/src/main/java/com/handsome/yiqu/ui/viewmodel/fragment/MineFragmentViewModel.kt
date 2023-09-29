package com.handsome.yiqu.ui.viewmodel.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.AuthorBean
import com.handsome.yiqu.bean.StatusBean
import com.handsome.yiqu.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MineFragmentViewModel : ViewModel() {

    private val _mutableUserInfo = MutableStateFlow<AuthorBean?>(null)
    val userInfo get() = _mutableUserInfo.asStateFlow()

    private val _mutableFollowUser = MutableStateFlow<StatusBean?>(null)
    val followUser get() = _mutableFollowUser.asStateFlow()

    fun getUserInfo(){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableUserInfo.emit(ApiService.INSTANCE.getUserInfo())
        }
    }

    fun followUser(toUserId : Long , actionId: Int){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableFollowUser.emit(ApiService.INSTANCE.followUser(toUserId,actionId))
        }
    }

}