package com.handsome.yiqu.ui.viewmodel.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.ApiWrapperUserBean
import com.handsome.yiqu.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonActivityViewModel : ViewModel() {

    private val _mutableUserInfo = MutableStateFlow<ApiWrapperUserBean?>(null)
    val userInfo get() = _mutableUserInfo.asStateFlow()

    fun getUserInfo(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableUserInfo.emit(ApiService.INSTANCE.getUserInfo(userId))
        }
    }


}