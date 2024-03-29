package com.handsome.module.video.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.lib.util.network.ApiStatus
import com.handsome.module.video.bean.CommentBean
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VideoApiService {

    @POST("favorite/action")
    fun updateLikeNum(
        @Query("video_id") videoId: Long,
        @Query("action_type") action_type: Int = 1,
    ): Single<ApiStatus>

    @GET("comment/list")
    suspend fun getVideoComment(
        @Query("video_id") videoId: Long,
    ): CommentBean

    @POST("comment/action")
    fun sendComment(
        @Query("video_id") videoId: Long,
        @Query("comment_text") commentText: String,
        @Query("action_type") action_type: Int = 1,
    ): Single<ApiStatus>

    // 关注操作
    @POST("relation/action")
    fun followUser(
        @Query("to_user_id") to_user_id: Long,
        @Query("action_type") action_type: Int,
    ):  Single<ApiStatus>

    companion object{
        val INSTANCE  by lazy {
            ApiGenerator.getApiService(VideoApiService::class)
        }
    }
}