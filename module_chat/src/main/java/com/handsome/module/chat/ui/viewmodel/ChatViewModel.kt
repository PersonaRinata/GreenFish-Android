package com.handsome.module.chat.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.module.chat.bean.ChatFriendsList
import com.handsome.module.chat.net.ChatApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : BaseViewModel() {

    private val _chatList = MutableStateFlow<ChatFriendsList?>(null)
    val chatList : StateFlow<ChatFriendsList?>
        get() = _chatList.asStateFlow()

    fun getChatList(userId : Long){
        viewModelScope.launch {
            _chatList.emit(ChatApiService.INSTANCE.getFriendChatList(userId))
        }
    }


}