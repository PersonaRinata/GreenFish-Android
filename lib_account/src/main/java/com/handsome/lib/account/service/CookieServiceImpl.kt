package com.handsome.lib.account.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.template.IProvider
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.handsome.lib.api.server.COOKIE_SERVICE
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


@Route(path = COOKIE_SERVICE)
class CookieServiceImpl : IProvider, CookieJar {

  private lateinit var mContext: Context

  private val mCookieJar by lazy {
    PersistentCookieJar(
      SetCookieCache(), SharedPrefsCookiePersistor(mContext)
    )
  }

  internal fun clearCookie() {
    mCookieJar.clear()
  }

  override fun init(context: Context) {
    mContext = context
  }

  override fun loadForRequest(url: HttpUrl): List<Cookie> {
    return mCookieJar.loadForRequest(url)
  }

  override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
    mCookieJar.saveFromResponse(url, cookies)
  }
}