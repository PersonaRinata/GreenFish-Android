package com.handsome.module.main.ui.viewmodel.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.main.bean.StatusBean
import com.handsome.module.main.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MineFragmentViewModel : ViewModel() {


    private val _mutableFollowUser = MutableStateFlow<StatusBean?>(null)
    val followUser get() = _mutableFollowUser.asStateFlow()

    fun followUser(toUserId : Long , actionId: Int){
        viewModelScope.launch(myCoroutineExceptionHandler) {
            _mutableFollowUser.emit(ApiService.INSTANCE.followUser(toUserId,actionId))
        }
    }

}