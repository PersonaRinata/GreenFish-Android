package com.handsome.module.aigc.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.aigc.bean.AskAiBean
import com.handsome.module.aigc.bean.AskAiHistoryBean
import com.handsome.module.aigc.bean.RecommendDoctorBean
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AigcApiService {

    @POST("aigc/question")
    fun askQuestion(@Query("content") content : String) : Single<AskAiBean>

    @GET("aigc/history")
    fun getAskAiHistory() : Single<AskAiHistoryBean>

    @GET("aigc/recommend/doctor")
    fun getRecommendDoctor(@Query("content") content : String) : Single<RecommendDoctorBean>

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(AigcApiService::class)
        }
    }

}