package com.handsome.module.main.ui.viewmodel.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.main.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivityViewModel : ViewModel() {

    private val _mutableLoginIn = MutableStateFlow<IAccountService.LoginBean?>(null)
    val loginIn get() = _mutableLoginIn.asStateFlow()

    private val _mutableRegisterIn = MutableStateFlow<IAccountService.LoginBean?>(null)
    val registerIn get() = _mutableRegisterIn.asStateFlow()

    private val _mutableUserInfo = MutableStateFlow<com.handsome.module.main.bean.ApiWrapperUserBean?>(null)
    val userInfo get() = _mutableUserInfo.asStateFlow()

    fun getUserInfo(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableUserInfo.emit(ApiService.INSTANCE.getUserInfo(userId))
        }
    }

    fun loginIn(userName: String, password: String) {
        viewModelScope.launch(myCoroutineExceptionHandler) {
            try {
                _mutableLoginIn.emit(ApiService.INSTANCE.loginIn(userName, password))
            }catch (e : Exception){
                toast("用户不存在")
            }
        }
    }

    fun registerIn(userName: String, password: String) {
        viewModelScope.launch {
            try {
                _mutableRegisterIn.emit(ApiService.INSTANCE.registerIn(userName, password))
            }catch (e : Exception){
                toast("注册失败")
            }
        }
    }

}