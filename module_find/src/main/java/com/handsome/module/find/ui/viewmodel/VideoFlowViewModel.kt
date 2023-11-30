package com.handsome.module.find.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.module.find.bean.VideoListBean
import com.handsome.module.find.net.FindApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class VideoFlowViewModel : ViewModel() {

    private val _findVideo = MutableLiveData<VideoListBean>()
    val findVideo : LiveData<VideoListBean>
        get() = _findVideo

    fun getFindVideo(){
        FindApiService.INSTANCE.getVideoList()
            .doOnError {
                toast(it.message)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _findVideo.value = it
        }
    }
}