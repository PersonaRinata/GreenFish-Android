package com.handsome.yiqu.ui.viewmodel.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.LoginBean
import com.handsome.yiqu.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivityViewModel : ViewModel() {

    private val _mutableLoginIn = MutableStateFlow<LoginBean?>(null)
    val loginIn get() = _mutableLoginIn.asStateFlow()

    private val _mutableRegisterIn = MutableStateFlow<LoginBean?>(null)
    val registerIn get() = _mutableRegisterIn.asStateFlow()

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