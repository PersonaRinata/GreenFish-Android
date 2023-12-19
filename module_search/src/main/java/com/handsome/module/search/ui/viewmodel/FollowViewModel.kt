package com.handsome.module.search.ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.handsome.api.video.bean.AuthorBean
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.search.bean.StatusBean
import com.handsome.module.search.net.Repository
import com.handsome.module.search.net.SearchApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FollowViewModel : BaseViewModel() {

    private val _mutableFollowUser = MutableStateFlow<StatusBean?>(null)
    val followUser get() = _mutableFollowUser.asStateFlow()


    fun searchUser(content : String) : Flow<PagingData<AuthorBean>> {
        return Repository.searchUser(content).cachedIn(viewModelScope)
    }

    fun followUser(toUserId : Long , actionId: Int){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableFollowUser.emit(SearchApiService.INSTANCE.followUser(toUserId,actionId))
        }
    }
}