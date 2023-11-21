package com.handsome.module.main.ui.viewmodel.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.main.bean.ApiWrapperUserBean
import com.handsome.module.main.net.ApiService
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