package com.handsome.lib.util.extention

import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException

inline fun <reified T : Any> Single<T>.catchException(
    crossinline action: (Throwable) -> Unit
): Single<T> {
    return onErrorResumeNext { throwable ->
        if (throwable is HttpException) {
            val response = throwable.response()
            if (response != null) {
                val body = response.errorBody()?.string()
                if (body != null) {
                    val gson = Gson()
                    val error = gson.fromJson(body, T::class.java)
                    Single.just(error)
                } else {
                    Single.error(throwable)
                }
            } else {
                Single.error(throwable)
            }
        } else {
            action(throwable)
            Single.error(throwable)
        }
    }
}