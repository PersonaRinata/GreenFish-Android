package com.handsome.lib.account.network

import com.handsome.lib.api.server.bean.LoginBean
import com.handsome.lib.util.network.ApiStatus
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

@Suppress("SpellCheckingInspection")
interface LoginApiService {
  
  @POST("user/login")
  fun login(
    @Query("username") username: String,
    @Query("password") password: String
  ): Single<LoginBean>

  @GET("user/logout/json")
  fun logout(): Single<ApiStatus>
  
  @POST("user/register")
  fun register(
    @Query("username") username: String,
    @Query("password") password: String,
    @Query("repassword") rePassword: String
  ): Single<LoginBean>
}