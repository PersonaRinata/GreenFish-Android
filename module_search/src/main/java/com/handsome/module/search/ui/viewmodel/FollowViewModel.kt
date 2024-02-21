package com.handsome.module.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.handsome.api.video.bean.AuthorBean
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.catchException
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.lib.util.network.ApiStatus
import com.handsome.module.search.net.Repository
import com.handsome.module.search.net.SearchApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

class FollowViewModel : BaseViewModel() {

    private val _mutableFollowUser = MutableLiveData<ApiStatus>()
    val followUser : LiveData<ApiStatus> get() = _mutableFollowUser


    fun searchUser(content : String) : Flow<PagingData<AuthorBean>> {
        return Repository.searchUser(content).cachedIn(viewModelScope)
    }

    fun followUser(toUserId : Long , actionId: Int){
        SearchApiService.INSTANCE.followUser(toUserId,actionId)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _mutableFollowUser.postValue(it)
            }
    }
}