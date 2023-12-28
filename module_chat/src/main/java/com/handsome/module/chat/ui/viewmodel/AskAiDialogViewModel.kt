package com.handsome.module.chat.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.module.chat.bean.AskAiResultBean
import com.handsome.module.chat.net.ChatApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AskAiDialogViewModel : BaseViewModel() {
    private val _askAi = MutableStateFlow<AskAiResultBean?>(null)
    val askAi : StateFlow<AskAiResultBean?>
        get() = _askAi.asStateFlow()

    fun askAi(content : String){
        viewModelScope.launch {
            _askAi.emit(ChatApiService.INSTANCE.getAskAiResult(content))
        }
    }
}