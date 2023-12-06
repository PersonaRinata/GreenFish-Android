package com.handsome.module.mine.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.handsome.api.video.bean.ApiWrapperUserBean
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.util.myCoroutineExceptionHandler
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

}