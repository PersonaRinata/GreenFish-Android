package com.handsome.module.mine.ui.viewmodel

import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.interceptHttpException
import com.handsome.module.mine.bean.FollowBean
import com.handsome.module.mine.bean.StatusBean
import com.handsome.module.mine.net.MineApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class FollowListViewModel : BaseViewModel() {

    private val _mutableFollowList = MutableStateFlow<FollowBean?>(null)
    val followList get() = _mutableFollowList.asStateFlow()

    private val _mutableFansList = MutableStateFlow<FollowBean?>(null)
    val fansList get() = _mutableFansList.asStateFlow()

    private val _mutableFollowUser = MutableStateFlow<StatusBean?>(null)
    val followUser get() = _mutableFollowUser.asStateFlow()

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
        flow {
            emit(MineApiService.INSTANCE.followUser(toUserId,actionId))
        }.interceptHttpException{}.collectLaunch {
            _mutableFollowUser.emit(it.copy())
        }
    }
}