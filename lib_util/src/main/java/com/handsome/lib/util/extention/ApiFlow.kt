package com.handsome.lib.util.extention

import com.google.gson.Gson
import com.handsome.lib.util.network.IApiWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

/**
 * 错误的执行action，正确的往下游发
 * 这里捕获到异常手动往下游发送信息
 * 使用方法:viewModel中
 * flow {
 *      emit(XXXApiService.INSTANCE.getXXX()))
 * }.interceptHttpException {}.collectLaunch {
 *      _mutableXXX.emit(it)
 * }
 */
inline fun <reified T : IApiWrapper> Flow<T>.interceptHttpException(
    crossinline action: suspend FlowCollector<T>.(Throwable) -> Unit
): Flow<T> = flow {
    try {
        collect { value ->
            return@collect emit(value)
        }
    } catch (e : HttpException) {
        action(e)
        val response = e.response()
        if (response != null) {
            val body = response.errorBody()?.string()
            if (body != null) {
                val gson = Gson()
                val error = gson.fromJson(body, T::class.java)
                emit(error)
            }
        }
    }
}



