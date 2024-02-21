package com.handsome.module.video.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.catchException
import com.handsome.lib.util.extention.interceptHttpException
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.lib.util.network.ApiStatus
import com.handsome.module.video.bean.CommentBean
import com.handsome.module.video.net.VideoApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class CommentDialogViewModel : BaseViewModel() {

    private val _mutableVideoComment = MutableStateFlow<CommentBean?>(null)
    val videoComment get() = _mutableVideoComment.asStateFlow()

    private val _mutableSendComment = MutableLiveData<ApiStatus>()
    val sendComment : LiveData<ApiStatus>
        get() = _mutableSendComment

    fun getVideoComment(videoId : Long){
        flow {
            emit(VideoApiService.INSTANCE.getVideoComment(videoId))
        }.interceptHttpException {}.collectLaunch {
            _mutableVideoComment.emit(it)
        }
    }

    fun sendComment(videoId : Long,commentText : String){
        VideoApiService.INSTANCE.sendComment(videoId,commentText)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _mutableSendComment.postValue(it)
            }
    }

}