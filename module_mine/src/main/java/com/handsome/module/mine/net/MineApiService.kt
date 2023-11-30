package com.handsome.module.mine.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.mine.bean.ApiWrapperUserBean
import com.handsome.module.mine.bean.FollowBean
import com.handsome.module.mine.bean.StatusBean
import com.handsome.module.mine.bean.VideoListBean
import retrofit2.http.GET
import retrofit2.http.POST
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

    // 获得关注列表
    @GET("relation/follow/list")
    suspend fun getFollowList(
        @Query("user_id") userId: Long,
    ) : FollowBean

    //获取粉丝列表
    @GET("relation/follower/list")
    suspend fun getFansList(
        @Query("user_id") userId: Long,
    ):  FollowBean

    // 关注操作
    @POST("relation/action")
    suspend fun followUser(
        @Query("to_user_id") to_user_id: Long,
        @Query("action_type") action_type: Int,
    ): StatusBean

    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService( MineApiService::class)
        }
    }

}