package com.handsome.module.mine.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.handsome.api.video.bean.ApiWrapperUserBean
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.mine.bean.JudgeDoctorBean
import com.handsome.module.mine.bean.StatusBean
import com.handsome.module.mine.net.MineApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.http.Part

class MineViewModel : BaseViewModel() {

    private val _mutableUserInfo = MutableStateFlow<ApiWrapperUserBean?>(null)
    val userInfo get() = _mutableUserInfo.asStateFlow()

    private val _uploadImg = MutableStateFlow<StatusBean?>(null)
    val uploadImg get() = _uploadImg.asStateFlow()

    private val _isDoctor = MutableStateFlow<JudgeDoctorBean?>(null)
    val isDoctor get() = _isDoctor.asStateFlow()

    private val _mutableFollowUser = MutableStateFlow<StatusBean?>(null)
    val followUser get() = _mutableFollowUser.asStateFlow()

    fun getUserInfo(userId : Long){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableUserInfo.emit(MineApiService.INSTANCE.getUserInfo(userId))
        }
    }

    fun uploadImg(@Part fileBody: MultipartBody.Part){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _uploadImg.emit(MineApiService.INSTANCE.uploadPhoto(fileBody))
        }
    }


    fun followUser(toUserId : Long,isFollow: Boolean){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            val actionId = if (isFollow) 2 else 1
            val resultBean = MineApiService.INSTANCE.followUser(toUserId,actionId)
            _mutableFollowUser.emit(StatusBean(resultBean.status_code,resultBean.status_msg+System.currentTimeMillis()))
        }
    }

}