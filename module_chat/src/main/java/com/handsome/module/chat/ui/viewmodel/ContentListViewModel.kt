package com.handsome.module.chat.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.chat.bean.ContentListBean
import com.handsome.module.chat.bean.IsDoctorBean
import com.handsome.module.chat.bean.IssueListBean
import com.handsome.module.chat.bean.StatusBean
import com.handsome.module.chat.bean.SumDiseaseBean
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

    private val _isDoctor = MutableStateFlow<IsDoctorBean?>(null)
    val isDoctor : StateFlow<IsDoctorBean?>
        get() = _isDoctor.asStateFlow()

    private val _issueList = MutableStateFlow<IssueListBean?>(null)
    val issueList : StateFlow<IssueListBean?>
        get() = _issueList.asStateFlow()

    private val _sumDisease = MutableStateFlow<SumDiseaseBean?>(null)
    val sumDisease : StateFlow<SumDiseaseBean?>
        get() = _sumDisease.asStateFlow()

    fun getContentList(otherId : Long,preTime : Long){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _contentList.emit(ChatApiService.INSTANCE.getContentList(otherId,preTime))
        }
    }

    fun uploadMessage(otherId: Long,content : String){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _uploadMessage.emit(ChatApiService.INSTANCE.uploadMessage(otherId,content))
        }
    }

    fun isDoctor(){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _isDoctor.emit(ChatApiService.INSTANCE.isDoctor())
        }
    }

    fun getIssueList(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _issueList.emit(ChatApiService.INSTANCE.getIssueList(userId))
        }
    }

    fun sumDisease(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _sumDisease.emit(ChatApiService.INSTANCE.getSumDisease(userId))
        }
    }
}