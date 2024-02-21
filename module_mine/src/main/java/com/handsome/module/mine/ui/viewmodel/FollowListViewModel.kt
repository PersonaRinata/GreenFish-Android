package com.handsome.module.mine.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.catchException
import com.handsome.lib.util.extention.interceptHttpException
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.module.mine.bean.FollowBean
import com.handsome.module.mine.bean.StatusBean
import com.handsome.module.mine.net.MineApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class FollowListViewModel : BaseViewModel() {

    private val _mutableFollowList = MutableStateFlow<FollowBean?>(null)
    val followList get() = _mutableFollowList.asStateFlow()

    private val _mutableFansList = MutableStateFlow<FollowBean?>(null)
    val fansList get() = _mutableFansList.asStateFlow()

    private val _mutableFollowUser = MutableLiveData<StatusBean>()
    val followUser : LiveData<StatusBean>
        get() = _mutableFollowUser

    fun getFollowList(userId : Long){
        flow {
            emit(MineApiService.INSTANCE.getFollowList(userId))
        }.interceptHttpException{}.collectLaunch {
            _mutableFollowList.emit(it)
        }
    }

    fun getFansList(userId : Long){
        flow {
            emit(MineApiService.INSTANCE.getFansList(userId))
        }.interceptHttpException{}.collectLaunch {
            _mutableFansList.emit(it)
        }
    }

    fun followUser(toUserId : Long , actionId: Int){
        MineApiService.INSTANCE.followUser(toUserId,actionId)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _mutableFollowUser.postValue(it)
            }
    }
}