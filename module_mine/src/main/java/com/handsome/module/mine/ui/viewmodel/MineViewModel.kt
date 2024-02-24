package com.handsome.module.mine.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handsome.api.video.bean.ApiWrapperUserBean
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.catchException
import com.handsome.lib.util.extention.interceptHttpException
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.module.mine.bean.StatusBean
import com.handsome.module.mine.net.MineApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.http.Part

class MineViewModel : BaseViewModel() {

    private val _mutableUserInfo = MutableStateFlow<ApiWrapperUserBean?>(null)
    val userInfo get() = _mutableUserInfo.asStateFlow()

    private val _uploadImg = MutableLiveData<StatusBean>()
    val uploadImg : LiveData<StatusBean>
        get() = _uploadImg

    private val _mutableFollowUser = MutableLiveData<StatusBean>()
    val followUser : LiveData<StatusBean>
        get() = _mutableFollowUser

    fun getUserInfo(userId: Long) {
        flow {
            emit(MineApiService.INSTANCE.getUserInfo(userId))
        }.interceptHttpException{}.collectLaunch {
            _mutableUserInfo.emit(it)
        }
    }

    fun uploadImg(@Part fileBody: MultipartBody.Part) {
        MineApiService.INSTANCE.uploadPhoto(fileBody)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _uploadImg.postValue(it)
            }
    }


    fun followUser(toUserId: Long, isFollow: Boolean) {
        val actionId = if (isFollow) 2 else 1
        MineApiService.INSTANCE.followUser(toUserId,actionId)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _mutableFollowUser.postValue(it)
            }
    }
}