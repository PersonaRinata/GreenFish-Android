package com.handsome.module.search.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.MAIN_SEARCH
import com.handsome.lib.api.server.service.ISearchService
import com.handsome.module.search.ui.activity.SearchActivity

@Route(name = MAIN_SEARCH, path = MAIN_SEARCH)
class SearchServiceImpl : ISearchService {
    private lateinit var mContext : Context

    override fun startSearchActivity() {
        SearchActivity.startAction(mContext)
    }

    override fun init(context: Context) {
        mContext = context
    }
}