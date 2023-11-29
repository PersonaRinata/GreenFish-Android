package com.handsome.module.mine.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.mine.bean.ApiWrapperUserBean
import com.handsome.module.mine.bean.VideoListBean
import retrofit2.http.GET
import retrofit2.http.Query

interface MineApiService {

    //获得喜欢信息的接口
    @GET("favorite/list")
    suspend fun getUserLike(
        @Query("user_id") userId: Long
    ):  VideoListBean

    //获得发布信息的接口
    @GET("publish/list")
    suspend fun getUserPublish(
        @Query("user_id") userId: Long,
    ): VideoListBean

    //获取个人信息的接口
    @GET("user")
    suspend fun getUserInfo(
        @Query("user_id") userId: Long
    ): ApiWrapperUserBean

    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService( MineApiService::class)
        }
    }

}