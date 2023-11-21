package com.handsome.lib.util.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    /**
     * 开启协程并收集 Flow
     */
    protected fun <T> Flow<T>.collectLaunch(action: suspend (value: T) -> Unit) {
        viewModelScope.launch {
            collect{ action.invoke(it) }
        }
    }
}