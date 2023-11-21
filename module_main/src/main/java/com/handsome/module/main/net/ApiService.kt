package com.handsome.module.main.net

import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.main.bean.CommentBean
import com.handsome.module.main.bean.ContentList
import com.handsome.module.main.bean.FollowBean
import com.handsome.module.main.bean.FriendsList
import com.handsome.module.main.bean.StatusBean
import com.handsome.module.main.bean.VideoListBean
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @GET("feed")
    suspend fun getAllVideo(@Query("token") token: String =  ApiService.Companion.token):  VideoListBean

    @GET("comment/list")
    suspend fun getVideoComment(
        @Query("video_id") videoId: Long,
        @Query("token") token: String =  ApiService.Companion.token
    ): CommentBean

    //todo 等待完成 ,记得返回值
    @POST("favorite/action")
    suspend fun updateLikeNum(
        @Query("video_id") videoId: Long,
        @Query("action_type") action_type: Int = 1,
        @Query("token") token: String = Companion.token
    ): StatusBean

    @POST("comment/action")
    suspend fun sendComment(
        @Query("video_id") videoId: Long,
        @Query("comment_text") commentText: String,
        @Query("action_type") action_type: Int = 1,
        @Query("token") token: String = ApiService.token
    ): StatusBean

    //获取个人信息的接口
    @GET("user")
    suspend fun getUserInfo(
        @Query("user_id") userId: Long,
        @Query("token") token: String = ApiService.token
    ): com.handsome.module.main.bean.ApiWrapperUserBean

    //获得喜欢信息的接口
    @GET("favorite/list")
    suspend fun getUserLike(
        @Query("user_id") userId: Long,
        @Query("token") token: String =  ApiService.Companion.token
    ):  VideoListBean

    //获得发布信息的接口
    @GET("publish/list")
    suspend fun getUserPublish(
        @Query("user_id") userId: Long,
        @Query("token") token: String =  ApiService.Companion.token
    ): VideoListBean

    // 上传视频 todo 等待返回数据
    @Multipart
    @POST("publish/action")
    suspend fun publishVideo(
        @Part video: MultipartBody.Part,
        @Query("title") title: String,
        @Query("token") token: String =  ApiService.Companion.token
    )

    //todo 获得发现的内容列表
    @POST("find/list")
    suspend fun getFindList(type: String): ContentList

    // 获得好友列表
    @GET("relation/friend/list")
    suspend fun getFriendsList(
        @Query("user_id") userId: Long =  ApiService.Companion.userId,
        @Query("token") token: String =  ApiService.Companion.token
    ): FriendsList

    // 关注操作
    @POST("relation/action")
    suspend fun followUser(
        @Query("to_user_id") to_user_id: Long,
        @Query("action_type") action_type: Int,
        @Query("token") token: String =  ApiService.Companion.token
    ):  StatusBean

    @POST("user/login")
    suspend fun loginIn(
        @Query("username") userName: String,
        @Query("password") password: String
    ): IAccountService.LoginBean

    @POST("user/register")
    suspend fun registerIn(
        @Query("username") userName: String,
        @Query("password") password: String
    ): IAccountService.LoginBean

    // 得到聊天记录
    @GET("message/chat")
    suspend fun getMessageHistory(
        @Query("to_user_id") toUserId: String,
        @Query("pre_msg_time") preMessageTime: String = "0",
        @Query("token") token: String =  ApiService.Companion.token
    ): com.handsome.module.main.bean.ApiWrapperChatMessageBean

    // 上传聊天记录
    @POST("message/action")
    suspend fun chatMessage(
        @Query("to_user_id") toUserId: String,
        @Query("content") content: String,
        @Query("action_type") action_type: Int = 1,
        @Query("token") token: String =  ApiService.Companion.token
    ):  StatusBean

    // 获得关注列表
    @GET("relation/follow/list")
    suspend fun getFollowList(
        @Query("user_id") userId: Long,
        @Query("token") token: String =  ApiService.Companion.token
    ) : FollowBean

    //获取粉丝列表
    @GET("relation/follower/list")
    suspend fun getFansList(
        @Query("user_id") userId: Long,
        @Query("token") token: String =  ApiService.Companion.token
    ):  FollowBean

    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService( ApiService::class)
        }

        var userId = 0L

        var token = ""
    }
}