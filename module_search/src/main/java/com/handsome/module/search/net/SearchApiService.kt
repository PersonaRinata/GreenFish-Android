package com.handsome.module.search.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.lib.util.network.ApiStatus
import com.handsome.module.search.bean.SearchUserBean
import com.handsome.module.search.bean.SearchVideoBean
import io.reactivex.rxjava3.core.Single
import retrofit2.http.POST
import retrofit2.http.Query

interface SearchApiService {

    @POST("video/search")
    suspend fun searchVideo(@Query("content") content : String,@Query("offset") offset : Int,@Query("num") num : Int) : SearchVideoBean

    @POST("user/search")
    suspend fun searchUser(@Query("content") content : String,@Query("offset") offset : Int,@Query("num") num : Int) : SearchUserBean

    // 关注操作
    @POST("relation/action")
    fun followUser(
        @Query("to_user_id") to_user_id: Long,
        @Query("action_type") action_type: Int,
    ): Single<ApiStatus>

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(SearchApiService::class)
        }
    }
}