package com.handsome.module.find.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.find.bean.VideoListBean
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface FindApiService {
    @GET("feed")
    fun getVideoList() : Single<VideoListBean>

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(FindApiService::class)
        }
    }
}