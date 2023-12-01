package com.handsome.module.mine.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.MAIN_MINE
import com.handsome.lib.api.server.service.IMineService
import com.handsome.lib.util.base.BaseFragment
import com.handsome.module.mine.ui.activity.PersonActivity
import com.handsome.module.mine.ui.fragment.MineFragment

@Route(path = MAIN_MINE)
class MineServiceImpl : IMineService {
    private lateinit var mContext: Context
    override fun getMineFragment(userId: Long): BaseFragment {
        return MineFragment.newInstance(userId)
    }

    override fun startPersonActivity(userId: Long) {
        PersonActivity.startAction(mContext,userId)
    }


    override fun init(context: Context) {
        mContext = context
    }
}