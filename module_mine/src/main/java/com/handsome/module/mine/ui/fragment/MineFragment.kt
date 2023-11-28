package com.handsome.module.mine.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.MAIN_MINE
import com.handsome.lib.util.base.BaseFragment
import com.handsome.module.mine.databinding.MineFragmentMineBinding

// 后续考虑通过service
@Route(path = MAIN_MINE)
class MineFragment : BaseFragment() {
    private val mBinding by lazy { MineFragmentMineBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}