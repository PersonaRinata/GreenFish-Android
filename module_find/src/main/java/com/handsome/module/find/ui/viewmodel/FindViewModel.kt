package com.handsome.module.find.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.module.find.bean.VideoBean
import com.handsome.module.find.net.FindApiService
import io.reactivex.rxjava3.schedulers.Schedulers

class FindViewModel : BaseViewModel() {

    private val _videoBean = MutableLiveData<VideoBean>()
    val videoBean : LiveData<VideoBean>
        get() = _videoBean

    fun getVideo(){
        FindApiService.INSTANCE.getVideoList()
            .doOnError {
                toast(it.message)
            }
            .subscribeOn(Schedulers.io())
            .unsafeSubscribeBy {
                _videoBean.postValue(it)
            }
    }
}