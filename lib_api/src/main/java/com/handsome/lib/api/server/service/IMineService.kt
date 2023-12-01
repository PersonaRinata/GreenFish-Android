package com.handsome.lib.api.server.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.handsome.lib.util.base.BaseFragment

interface IMineService : IProvider {

    fun getMineFragment(userId : Long) : BaseFragment

    fun startPersonActivity(userId: Long)

}