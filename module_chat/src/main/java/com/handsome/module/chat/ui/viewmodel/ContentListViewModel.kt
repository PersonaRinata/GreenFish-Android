package com.handsome.module.chat.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.module.chat.bean.ContentListBean
import com.handsome.module.chat.bean.StatusBean
import com.handsome.module.chat.net.ChatApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContentListViewModel: BaseViewModel() {

    private val _contentList = MutableStateFlow<ContentListBean?>(null)
    val contentList : StateFlow<ContentListBean?>
        get() = _contentList.asStateFlow()

    private val _uploadMessage = MutableStateFlow<StatusBean?>(null)
    val uploadMessage : StateFlow<StatusBean?>
        get() = _uploadMessage.asStateFlow()

    fun getContentList(otherId : Long,preTime : Long){
        viewModelScope.launch {
            _contentList.emit(ChatApiService.INSTANCE.getContentList(otherId,preTime))
        }
    }

    fun uploadMessage(otherId: Long,content : String){
        viewModelScope.launch {
            _uploadMessage.emit(ChatApiService.INSTANCE.uploadMessage(otherId,content))
        }
    }
}