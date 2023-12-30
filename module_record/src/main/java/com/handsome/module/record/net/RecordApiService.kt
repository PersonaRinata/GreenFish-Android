package com.handsome.module.record.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.record.bean.ApiWrapperUserBean
import com.handsome.module.record.bean.IssueListBean
import com.handsome.module.record.bean.StatusBean
import com.handsome.module.record.bean.UpdateIssueListBean
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RecordApiService {
    @GET("issuelist")
    fun getIssueList(@Query("user_id") userId : Long) : Single<IssueListBean>

    @POST("issuelist/action")
    fun updateIssueList(@Body issueListBean: UpdateIssueListBean) : Single<StatusBean>

    //获取个人信息的接口
    @GET("user")
    fun getUserInfo(
        @Query("user_id") userId: Long
    ): Single<ApiWrapperUserBean>

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(RecordApiService::class)
        }
    }
}