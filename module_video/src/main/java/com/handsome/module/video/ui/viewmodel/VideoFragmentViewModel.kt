package com.handsome.module.video.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.catchException
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.lib.util.network.ApiStatus
import com.handsome.module.video.net.VideoApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class VideoFragmentViewModel : BaseViewModel() {

    private val _mutableVideoLike = MutableLiveData<ApiStatus>()
    val videoLike : LiveData<ApiStatus>
        get() = _mutableVideoLike

    private val _mutableFollowUser = MutableLiveData<ApiStatus>()
    val followUser : LiveData<ApiStatus>
        get() = _mutableFollowUser

    // 第二个参数传递的是当前是否喜欢这个视频，如果喜欢那么操作就是取消喜欢2。
    // 第三个参数是当前喜欢的数量
    fun updateLikeNum(videoId: Long, isFavorite: Boolean) {
        val actionId = if (isFavorite) 2  else  1
        VideoApiService.INSTANCE.updateLikeNum(videoId, actionId)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _mutableVideoLike.postValue(it)
            }
    }

    fun followUser(toUserId: Long, isFollow : Boolean) {
        // 1 是喜欢操作， 2 是不喜欢
        // 由于结果是一样的，没有引发数据的改变，所以不会再次发射数据，又因为stateflow自己会比较新数据和旧数据的差别，所以用distinctUntilChanged没用
        val actionId = if (isFollow) 2  else  1
        VideoApiService.INSTANCE.followUser(toUserId, actionId)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _mutableFollowUser.postValue(it)
            }
    }

}