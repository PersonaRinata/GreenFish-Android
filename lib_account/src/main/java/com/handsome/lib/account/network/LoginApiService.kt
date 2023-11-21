package com.handsome.lib.account.network

import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.util.network.ApiStatus
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

@Suppress("SpellCheckingInspection")
interface LoginApiService {
  
  @POST("/user/login")
  @FormUrlEncoded
  fun login(
    @Field("username") username: String,
    @Field("password") password: String
  ): Single<IAccountService.LoginBean>
  
  @GET("/user/logout/json")
  fun logout(): Single<ApiStatus>
  
  @POST("/user/register")
  @FormUrlEncoded
  fun register(
    @Field("username") username: String,
    @Field("password") password: String,
    @Field("repassword") rePassword: String
  ): Single<IAccountService.LoginBean>
}