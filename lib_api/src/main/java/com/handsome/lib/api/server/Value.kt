package com.handsome.lib.api.server

import java.io.Serializable

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