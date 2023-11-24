package com.handsome.lib.util.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

//const val COOKIE_SERVICE = "/cookie/service"

object ApiGenerator {
    // 获取用户信息变得很简单，直接IAccountService获取实例直接有
    // 但是初始化token的过程变得离谱
    private var token : String? = null

    fun setToken(token : String){
        this.token = token
    }

    fun getToken() : String?{
        return token
    }

    private val retrofit = getNewRetrofit(false){
        addInterceptor(Interceptor { chain ->
            val original: Request = chain.request()
            val request: Request = original.newBuilder().apply {
                token?.let { header("token", token!!) }  //如果还是没有的话直接返回登录过期，要求重新登录
                method(original.method, original.body)
            }
                .build()
            chain.proceed(request)
        })
    }

    //3getApiService 函数：创建给定类的 API 服务，就是将我们的网络请求接口实例化
    fun <T : Any> getApiService(clazz: KClass<T>): T {
        return retrofit.create(clazz.java)
    }

    /**
     * 创建一个新的 Retrofit
     */
    fun getNewRetrofit(
        isNeedCookie: Boolean,
        config: (OkHttpClient.Builder.() -> Unit)? = null
    ): Retrofit {
//        val cookieJar: CookieJar = CookieJarImpl
        Log.d("TAG", "getNewRetrofit: = ${getBaseUrl()}")
        return Retrofit
            .Builder()
            .baseUrl(getBaseUrl())
            .client(OkHttpClient().newBuilder().run {
//                cookieJar(cookieJar)
                config?.invoke(this)
                defaultOkhttpConfig(isNeedCookie)
            })
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
            .build()
    }

    //4defaultOkhttpConfig 函数：使用默认设置配置 OkHttpClient.Builder
    private fun OkHttpClient.Builder.defaultOkhttpConfig(isNeedCookie: Boolean): OkHttpClient {
        connectTimeout(5000, TimeUnit.SECONDS)
        readTimeout(5000, TimeUnit.SECONDS)
        addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        return build()
    }

    //5apiResultList：一个线程安全的列表，用于存储 ApiResult 对象，有助于调试。
    // 用于 debug 时保存网络请求，因为异常触发时进程被摧毁，Pandora 记录的请求会被清空
    val apiResultList by lazy {
        CopyOnWriteArrayList<ApiResult>()
    }

    //6ApiResult 类：表示 API 请求的结果，包括请求、响应以及任何异常的堆栈跟踪。
    class ApiResult(var request: Request) {
        var response: Response? = null
        var stackTrace: String? = null // 直接保存 throwable 对象的话，不会被记录下来，不知道为什么
    }
}

//7包括一个 MAP 属性，用于存储 API 服务的实例。
//扩展属性 <I : IApi> KClass<I>.api：
interface IApi {
    companion object {
        val MAP = HashMap<KClass<out IApi>, IApi>()
    }
}

/**
 * 带 cookie 的请求
 */
//8为实现 IApi 的类提供一个扩展属性，用于创建带有 cookie 的 API 服务。
//在 IApi.MAP 中惰性地初始化和缓存 API 服务实例。
@Suppress("UNCHECKED_CAST")
val <I : IApi> KClass<I>.api: I
    get() = IApi.MAP.getOrPut(this) {
        ApiGenerator.getApiService(this)
    } as I
//9  getOrPut方法，相当于在map中的一种懒加载，就是如果没有这个值，
//就通过我们传入的lambda表达式创建一个值，如果存在，就不调用