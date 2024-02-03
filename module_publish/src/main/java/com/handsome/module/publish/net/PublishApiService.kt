package com.handsome.module.publish.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.lib.util.network.ApiStatus
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PublishApiService {

    @Multipart
    @POST("publish/action")
    suspend fun publishVideo(
        @Part data: MultipartBody.Part,
        @Part("title") title: String,
    ) : ApiStatus


    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(PublishApiService::class)
        }
    }
}