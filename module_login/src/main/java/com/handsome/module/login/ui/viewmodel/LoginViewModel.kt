package com.handsome.module.login.ui.viewmodel

import androidx.core.content.edit
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.exception.ApiException
import com.handsome.lib.util.service.ServiceManager
import com.handsome.lib.util.service.impl
import com.handsome.lib.util.util.getSp
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : BaseViewModel(){
    /**
     * 流程: 记住密码->存进去sp->取出时判断登陆状态
     * 登录->放入text中
     * 退出登录->账号密码清空
     */
    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?>
        get() = _username.asStateFlow()

    private val _password = MutableStateFlow<String?>(null)
    val password: StateFlow<String?>
        get() = _password.asStateFlow()

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent: SharedFlow<LoginEvent>
        get() = _loginEvent

    private val mLoginSp = getSp(this::class.java.simpleName)
//    private val mAccountService = IAccountService::class.impl

    fun login(userName : String,password : String){
//        val loginBean = mAccountService.login(userName,password)
    }

    fun isRememberPassword(): Boolean {
        return mLoginSp.getBoolean("isRememberPassword", false)
    }

    fun changeRememberPassword(isRemember: Boolean) {
        mLoginSp.edit {
            putBoolean("isRememberPassword", isRemember)
        }
    }

    sealed class LoginEvent {
        data class Success(val bean: IAccountService.LoginBean): LoginEvent()
        data class Fail(val error: ApiException): LoginEvent()
    }
}