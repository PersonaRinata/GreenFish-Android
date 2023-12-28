package com.handsome.module.chat.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.chat.bean.AskAiResultBean
import com.handsome.module.chat.bean.ChatFriendsList
import com.handsome.module.chat.bean.ContentListBean
import com.handsome.module.chat.bean.IsDoctorBean
import com.handsome.module.chat.bean.IssueListBean
import com.handsome.module.chat.bean.StatusBean
import com.handsome.module.chat.bean.SumDiseaseBean
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApiService {

    @GET("relation/friend/list/")
    suspend fun getFriendChatList(@Query("user_id") userId : Long) : ChatFriendsList

    @GET("message/chat/")
    suspend fun getContentList(@Query("to_user_id") otherId : Long,@Query("pre_msg_time") time : Long) : ContentListBean

    @POST("message/action/")
    suspend fun uploadMessage(@Query("to_user_id") otherId: Long,@Query("content") content : String,@Query("action_type") type : Int = 1) : StatusBean

    @GET("user/judge/doctor")
    suspend fun isDoctor() : IsDoctorBean

    @GET("issuelist")
    suspend fun getIssueList(@Query("user_id") userId : Long) : IssueListBean

    @GET("aigc/issuelist")
    suspend fun getSumDisease(@Query("user_id") userId : Long) : SumDiseaseBean

    @POST("aigc/word")
    suspend fun getAskAiResult(@Query("content") content: String) : AskAiResultBean

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(ChatApiService::class)
        }
    }
}