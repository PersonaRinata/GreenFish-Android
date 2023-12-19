package com.handsome.module.search.ui.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.handsome.api.video.bean.VideoBean
import com.handsome.module.search.net.SearchApiService

class VideoFlowPagingSource(private val content : String) : PagingSource<Int,VideoBean>() {

    override fun getRefreshKey(state: PagingState<Int, VideoBean>): Int?=null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoBean> {
        return try {
            val page = params.key ?: 1 // set page 1 as default
            val pageSize = params.loadSize
            val repoResponse = SearchApiService.INSTANCE.searchVideo(content,(page-1) * pageSize,pageSize)
            if (repoResponse.status_code != 0) throw Exception("请求异常")
            val repoItems = repoResponse.video_list
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (repoItems.isNotEmpty()) page + 1 else null
            LoadResult.Page(repoItems, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}