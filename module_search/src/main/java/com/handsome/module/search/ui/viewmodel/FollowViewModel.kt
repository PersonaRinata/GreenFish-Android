package com.handsome.module.search.ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.handsome.api.video.bean.AuthorBean
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.interceptHttpException
import com.handsome.lib.util.network.ApiStatus
import com.handsome.module.search.net.Repository
import com.handsome.module.search.net.SearchApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class FollowViewModel : BaseViewModel() {

    private val _mutableFollowUser = MutableStateFlow<ApiStatus?>(null)
    val followUser get() = _mutableFollowUser.asStateFlow()


    fun searchUser(content : String) : Flow<PagingData<AuthorBean>> {
        return Repository.searchUser(content).cachedIn(viewModelScope)
    }

    fun followUser(toUserId : Long , actionId: Int){
        flow {
            emit(SearchApiService.INSTANCE.followUser(toUserId,actionId))
        }.interceptHttpException {}.collectLaunch {
            _mutableFollowUser.emit(it.copy())
        }
    }
}