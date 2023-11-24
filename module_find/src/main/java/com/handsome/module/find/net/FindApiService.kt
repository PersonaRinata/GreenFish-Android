package com.handsome.module.find.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.find.bean.VideoBean
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface FindApiService {
    @GET("feed")
    fun getVideoList() : Single<VideoBean>

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(FindApiService::class)
        }
    }
}