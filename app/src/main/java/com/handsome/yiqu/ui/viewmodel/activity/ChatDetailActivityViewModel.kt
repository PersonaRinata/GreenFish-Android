package com.handsome.yiqu.ui.viewmodel.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.ApiWrapperChatMessageBean
import com.handsome.yiqu.bean.StatusBean
import com.handsome.yiqu.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatDetailActivityViewModel : ViewModel(){

    private val _mutableMessageHistory = MutableStateFlow<ApiWrapperChatMessageBean?>(null)
    val messageHistory get() = _mutableMessageHistory.asStateFlow()

    private val _mutableChatMessage = MutableStateFlow<StatusBean?>(null)
    val chatMessage get() = _mutableChatMessage.asStateFlow()

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
}