package com.handsome.yiqu.ui.viewmodel.fragment


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.VideoListBean
import com.handsome.yiqu.net.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecommendFragmentViewModel : ViewModel() {

    private val _mutableVideoList = MutableStateFlow<VideoListBean?>(null)
    val videoList get() = _mutableVideoList.asStateFlow()

    // 这个注解的原因是因为
    fun getVideoList(){
        viewModelScope.launch(myCoroutineExceptionHandler){
            _mutableVideoList.emit(ApiService.INSTANCE.getAllVideo())
        }
    }
}