package com.handsome.module.main.ui.viewmodel.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.main.bean.StatusBean
import com.handsome.module.main.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatDetailActivityViewModel : ViewModel(){

    private val _mutableMessageHistory = MutableStateFlow<com.handsome.module.main.bean.ApiWrapperChatMessageBean?>(null)
    val messageHistory get() = _mutableMessageHistory.asStateFlow()

    private val _mutableChatMessage = MutableStateFlow<StatusBean?>(null)
    val chatMessage get() = _mutableChatMessage.asStateFlow()

    private val _mutableUserInfo = MutableStateFlow<com.handsome.module.main.bean.ApiWrapperUserBean?>(null)
    val userInfo get() = _mutableUserInfo.asStateFlow()

    fun getMessageHistory(toUserId : String,preMessageTime : String = "0"){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableMessageHistory.emit(ApiService.INSTANCE.getMessageHistory(toUserId,preMessageTime))
        }
    }

    fun chatMessage(toUserId : String,content : String){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableChatMessage.emit(ApiService.INSTANCE.chatMessage(toUserId,content))
        }
    }

    fun getUserInfo(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableUserInfo.emit(ApiService.INSTANCE.getUserInfo(userId))
        }
    }
}