package com.handsome.yiqu.ui.viewmodel.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.FriendsList
import com.handsome.yiqu.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatFragmentViewModel : ViewModel() {

    private val _mutableFriendsList = MutableStateFlow<FriendsList?>(null)
    val friendsList = _mutableFriendsList.asStateFlow()

    fun getFriendsList(){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableFriendsList.emit(ApiService.INSTANCE.getFriendsList())
            //todo
//            _mutableFriendsList.emit(fakeMessage)
        }
    }

    private val fakeMessage = FriendsList(
        status_msg = "",
        status_code = 0,
        user_list = listOf(
            FriendsList.User(
              avatar = "",
              background_image = "",
              5,
              5,
              5,
              1231313131,
              true,
              "你吃饭了吗？",
              1,
              "开心超人",
                "我是一个不普通的签名",
                123,
                12
            ),
            FriendsList.User(
                avatar = "",
                background_image = "",
                5,
                5,
                5,
                1231313131,
                true,
                "你吃饭了吗？",
                1,
                "16654564",
                "我是一个不普通的签名",
                123,
                12
            ),
            FriendsList.User(
                avatar = "",
                background_image = "",
                5,
                5,
                5,
                1231313131,
                true,
                "猪猪侠在哪？",
                1,
                "菲菲公主",
                "我是一个不普通的签名",
                123,
                12
            ),
            FriendsList.User(
                avatar = "",
                background_image = "",
                5,
                5,
                5,
                1231313131,
                true,
                "如何速通小怪兽",
                1,
                "奥特曼",
                "我是一个不普通的签名",
                123,
                12
            ),
            FriendsList.User(
                avatar = "",
                background_image = "",
                5,
                5,
                5,
                1231313131,
                true,
                "1+1=？",
                1,
                "天才",
                "我是一个不普通的签名",
                123,
                12
            ),
        )
    )
}