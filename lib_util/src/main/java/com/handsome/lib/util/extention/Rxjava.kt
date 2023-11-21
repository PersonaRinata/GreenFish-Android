@file:Suppress("HasPlatformType")

package com.handsome.lib.util.extention

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import java.io.Serializable

/**
 * 这个命名与之前的 lib_common 中有些区别，common 中那个 safe 的意思是如果直接使用一个形参的 subscribe(onSuccess)，
 * 在收到上游错误时 Rxjava 会把错误直接抛给整个应用来处理，如果你没有配置 Rxjava 的全局报错，应用会直接闪退，
 * common 中 safe 就是指上述安全问题
 *
 * 目前这个 safe 表示带有生命周期的安全订阅
 */

fun <T : Any> Single<T>.unsafeSubscribeBy(
  onError: (Throwable) -> Unit = {},
  onSuccess: (T) -> Unit = {}
): Disposable = subscribe(onSuccess, onError)
fun <T : Any> Maybe<T>.unsafeSubscribeBy(
  onError: (Throwable) -> Unit = {},
  onComplete: () -> Unit = {},
  onSuccess: (T) -> Unit = {}
): Disposable = subscribe(onSuccess, onError, onComplete)
fun <T : Any> Observable<T>.unsafeSubscribeBy(
  onError: (Throwable) -> Unit = {},
  onComplete: () -> Unit = {},
  onNext: (T) -> Unit = {}
): Disposable = subscribe(onNext, onError, onComplete)
fun <T : Any> Flowable<T>.unsafeSubscribeBy(
  onError: (Throwable) -> Unit = {},
  onComplete: () -> Unit = {},
  onNext: (T) -> Unit = {}
): Disposable = subscribe(onNext, onError, onComplete)
fun Completable.unsafeSubscribeBy(
  onError: (Throwable) -> Unit = {},
  onComplete: () -> Unit = {},
): Disposable = subscribe(onComplete, onError)


/**
 * 不是很推荐你使用它，虽然很方便，但你知道这个的原理吗？
 */
fun <T: Any> Observable<T>.setSchedulers(
  subscribeOn: Scheduler = Schedulers.io(),
  unsubscribeOn: Scheduler = Schedulers.io(),
  observeOn: Scheduler = AndroidSchedulers.mainThread()
): Observable<T> = subscribeOn(subscribeOn)
  .unsubscribeOn(unsubscribeOn)
  .observeOn(observeOn)



/**
 * 用来解决 Rxjava 不允许传递 null 值的包裹类
 */
data class Value<T : Any>(val value: T?): Serializable {
  
  fun isNull(): Boolean = value == null
  
  fun isNotNull(): Boolean = !isNull()
  
  inline fun nullUnless(block: (T) -> Unit): Value<T> {
    if (value != null) block.invoke(value)
    return this
  }
  
  inline fun <E> nullUnless(default: E, block: (T) -> E): E {
    return if (value != null) block.invoke(value) else default
  }
  
  inline fun nullIf(block: () -> Unit): Value<T> {
    if (value == null) block.invoke()
    return this
  }
  
  inline fun <E> nullIf(default: E, block: () -> E): E {
    return if (value == null) block.invoke() else default
  }
}
