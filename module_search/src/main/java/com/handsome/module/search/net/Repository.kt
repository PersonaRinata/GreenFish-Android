package com.handsome.module.search.net

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.handsome.api.video.bean.AuthorBean
import com.handsome.api.video.bean.VideoBean
import com.handsome.module.search.ui.adapter.FollowPagingSource
import com.handsome.module.search.ui.adapter.VideoFlowPagingSource
import kotlinx.coroutines.flow.Flow

object Repository {

    private const val PAGE_SIZE = 5

    fun searchVideo(content : String) : Flow<PagingData<VideoBean>>{
        Log.d("lx", "searchVideo: ")
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { VideoFlowPagingSource(content) }
        ).flow
    }

    fun searchUser(content : String) : Flow<PagingData<AuthorBean>>{
        Log.d("lx", "searchUser: ")
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { FollowPagingSource(content) }
        ).flow
    }
}