package com.handsome.lib.util.base

import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class BaseDialogFragment : DialogFragment() {
    fun <T : Any> arguments() = ArgumentHelper<T>{ requireArguments() }

    fun <T> Flow<T>.collectLaunch(
        owner: LifecycleOwner = viewLifecycleOwner,
        action: suspend (value: T) -> Unit
    ) {
        owner.lifecycleScope.launch {
            collect{ action.invoke(it) }
        }
    }
}