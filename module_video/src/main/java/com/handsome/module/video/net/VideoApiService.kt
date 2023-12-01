package com.handsome.module.video.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.video.bean.CommentBean
import com.handsome.module.video.bean.StatusBean
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VideoApiService {

    @POST("favorite/action")
    suspend fun updateLikeNum(
        @Query("video_id") videoId: Long,
        @Query("action_type") action_type: Int = 1,
    ): StatusBean

    @GET("comment/list")
    suspend fun getVideoComment(
        @Query("video_id") videoId: Long,
    ): CommentBean

    @POST("comment/action")
    suspend fun sendComment(
        @Query("video_id") videoId: Long,
        @Query("comment_text") commentText: String,
        @Query("action_type") action_type: Int = 1,
    ): StatusBean

    // 关注操作
    @POST("relation/action")
    suspend fun followUser(
        @Query("to_user_id") to_user_id: Long,
        @Query("action_type") action_type: Int,
    ):  StatusBean

    companion object{
        val INSTANCE  by lazy {
            ApiGenerator.getApiService(VideoApiService::class)
        }
    }
}