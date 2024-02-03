package com.handsome.module.chat.ui.viewmodel

import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.interceptHttpException
import com.handsome.lib.util.network.ApiStatus
import com.handsome.module.chat.bean.ContentListBean
import com.handsome.module.chat.bean.IsDoctorBean
import com.handsome.module.chat.bean.IssueListBean
import com.handsome.module.chat.bean.SumDiseaseBean
import com.handsome.module.chat.net.ChatApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class ContentListViewModel: BaseViewModel() {

    private val _contentList = MutableStateFlow<ContentListBean?>(null)
    val contentList : StateFlow<ContentListBean?>
        get() = _contentList.asStateFlow()

    private val _uploadMessage = MutableStateFlow<ApiStatus?>(null)
    val uploadMessage : StateFlow<ApiStatus?>
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
        flow {
            emit(ChatApiService.INSTANCE.getContentList(otherId,preTime))
        }.interceptHttpException{}.collectLaunch {
            _contentList.emit(it)
        }
    }

    fun uploadMessage(otherId: Long,content : String){
        flow {
            emit(ChatApiService.INSTANCE.uploadMessage(otherId,content))
        }.interceptHttpException{}.collectLaunch {
            _uploadMessage.emit(it)
        }
    }

    fun isDoctor(){
        flow {
            emit(ChatApiService.INSTANCE.isDoctor())
        }.interceptHttpException{}.collectLaunch {
            _isDoctor.emit(it)
        }
    }

    fun getIssueList(userId : Long){
        flow {
            emit(ChatApiService.INSTANCE.getIssueList(userId))
        }.interceptHttpException{}.collectLaunch {
            _issueList.emit(it)
        }
    }

    fun sumDisease(userId : Long){
        flow {
            emit(ChatApiService.INSTANCE.getSumDisease(userId))
        }.interceptHttpException{}.collectLaunch {
            _sumDisease.emit(it)
        }
    }
}