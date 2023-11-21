package com.handsome.module.main.ui.viewmodel.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.main.bean.FriendsList
import com.handsome.module.main.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatFragmentViewModel : ViewModel() {

    private val _mutableFriendsList = MutableStateFlow<FriendsList?>(null)
    val friendsList = _mutableFriendsList.asStateFlow()

    fun getFriendsList(){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableFriendsList.emit(ApiService.INSTANCE.getFriendsList())
        }
    }
}