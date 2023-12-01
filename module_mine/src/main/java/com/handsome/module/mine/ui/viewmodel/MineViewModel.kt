package com.handsome.module.mine.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.handsome.api.video.bean.ApiWrapperUserBean
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.mine.net.MineApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MineViewModel : BaseViewModel() {

    private val _mutableUserInfo = MutableStateFlow<ApiWrapperUserBean?>(null)
    val userInfo get() = _mutableUserInfo.asStateFlow()

    fun getUserInfo(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableUserInfo.emit(MineApiService.INSTANCE.getUserInfo(userId))
        }
    }

}