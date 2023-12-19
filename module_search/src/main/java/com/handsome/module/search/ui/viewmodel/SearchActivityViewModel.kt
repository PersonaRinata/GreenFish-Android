package com.handsome.module.search.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchActivityViewModel : BaseViewModel() {

    private val _searchContent = MutableStateFlow<String?>(null)
    val searchContent get() = _searchContent.asStateFlow()

    fun setSearchContent(content : String){
        viewModelScope.launch {
            _searchContent.emit(content)
        }
    }
}