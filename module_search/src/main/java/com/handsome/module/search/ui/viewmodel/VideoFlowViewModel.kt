package com.handsome.module.search.ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.handsome.api.video.bean.VideoBean
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.module.search.net.Repository
import kotlinx.coroutines.flow.Flow

class VideoFlowViewModel : BaseViewModel() {

    fun searchVideo(content : String) : Flow<PagingData<VideoBean>>{
        return Repository.searchVideo(content).cachedIn(viewModelScope)
    }
}