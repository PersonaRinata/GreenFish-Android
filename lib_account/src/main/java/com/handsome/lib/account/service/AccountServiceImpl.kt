package com.handsome.lib.account.service

import android.content.Context
import androidx.core.content.edit
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.handsome.lib.account.network.LoginApiService
import com.handsome.lib.account.table.LOGIN_INFO
import com.handsome.lib.account.table.USER_INFO
import com.handsome.lib.api.server.ACCOUNT_SERVICE
import com.handsome.lib.api.server.Value
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.lib.util.network.ApiGenerator
import com.handsome.lib.util.service.impl
import com.handsome.lib.util.util.getSp
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/5/29 22:25
 */
@Route(name = ACCOUNT_SERVICE,path = ACCOUNT_SERVICE)
class AccountServiceImpl : IAccountService {
  
  private lateinit var mContext: Context

  private val mCookieService = CookieServiceImpl::class.impl
  
  private val mApiService by lazy(LazyThreadSafetyMode.NONE) {
    ApiGenerator.getNewRetrofit(false) {
      cookieJar(mCookieService)
    }.create(LoginApiService::class.java)
  }

  override fun observeUserInfoState(): Observable<Value<IAccountService.LoginBean>> {
    return userInfoState.distinctUntilChanged()
  }

  override fun observeUserInfoEvent(): Observable<Value<IAccountService.LoginBean>> {
    return userInfoEvent.distinctUntilChanged()
  }
  
  override fun getUserInfo(): IAccountService.LoginBean? {
    return userInfoState.value?.value
  }
  
  private val userInfoState = BehaviorSubject.create<Value<IAccountService.LoginBean>>()
  private val userInfoEvent = PublishSubject.create<Value<IAccountService.LoginBean>>()
  
  private fun emitUserInfo(bean: IAccountService.LoginBean?) {
    val value = Value(bean)
    userInfoState.onNext(value)
    userInfoEvent.onNext(value)
  }
  
  override fun isLogin(): Boolean {
    return ApiGenerator.getToken() != null
  }
  
  override fun login(
    username: String,
    password: String
  ): Single<IAccountService.LoginBean> {
    return mApiService.login(username, password)
      .doOnSuccess {
        emitUserInfo(it.copy(username = username,password = password))
      }.subscribeOn(Schedulers.io())
      .map { it }
  }
  
  override fun logout(): Completable {
    return mApiService.logout()
      .doOnSuccess {
        mCookieService.clearCookie()
        emitUserInfo(null)
      }.flatMapCompletable {
        Completable.complete()
      }.subscribeOn(Schedulers.io())
  }
  
  override fun register(
    username: String,
    password: String,
    rePassword: String
  ): Single<IAccountService.LoginBean> {
    return mApiService.register(username, password, rePassword)
      .doOnSuccess {
        emitUserInfo(it.copy(username = username,password = password))
      }.subscribeOn(Schedulers.io())
      .map { it }
  }
  
  override fun init(context: Context) {
    mContext = context
    val userinfoSp = getSp(USER_INFO)
    val gson = Gson()
    val spKey = LOGIN_INFO
    // 从本地初始化数据
    val userinfo = userinfoSp.getString(spKey, null)
    try {
      val loginBean: IAccountService.LoginBean? =
        gson.fromJson(userinfo, IAccountService.LoginBean::class.java)
      if (loginBean != null) {
        emitUserInfo(loginBean)
      }
    } catch (e: JsonSyntaxException) {
      e.printStackTrace()
      userinfoSp.edit { remove(spKey) }
    }
    observeUserInfoEvent()
      .unsafeSubscribeBy {
        userinfoSp.edit {
          putString(spKey, gson.toJson(it))
        }
      }
  }
}