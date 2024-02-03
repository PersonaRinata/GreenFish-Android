package com.handsome.module.mine.ui.viewmodel

import com.handsome.api.video.bean.ApiWrapperUserBean
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.interceptHttpException
import com.handsome.module.mine.bean.JudgeDoctorBean
import com.handsome.module.mine.bean.StatusBean
import com.handsome.module.mine.net.MineApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
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

    fun getUserInfo(userId: Long) {
        flow {
            emit(MineApiService.INSTANCE.getUserInfo(userId))
        }.interceptHttpException{}.collectLaunch {
            _mutableUserInfo.emit(it)
        }
    }

    fun uploadImg(@Part fileBody: MultipartBody.Part) {
        flow {
            emit(MineApiService.INSTANCE.uploadPhoto(fileBody))
        }.interceptHttpException {}.collectLaunch {
            _uploadImg.emit(it)
        }
    }


    fun followUser(toUserId: Long, isFollow: Boolean) {
        val actionId = if (isFollow) 2 else 1
        flow {
            emit(MineApiService.INSTANCE.followUser(toUserId, actionId))
        }.interceptHttpException {}.collectLaunch {
            _mutableFollowUser.emit(it.copy())
        }
    }
}