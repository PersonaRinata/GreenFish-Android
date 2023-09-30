package com.handsome.yiqu.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseFragment
import com.handsome.yiqu.bean.ContentType
import com.handsome.yiqu.databinding.MainFragmentFindBinding

class FindFragment : BaseFragment() {
    private val mBinding by lazy { MainFragmentFindBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initVp()
        initTabLayout()
        return mBinding.root
    }

    private fun initTabLayout() {
        // 取消tabLayout的长按点击事件
        with(mBinding.mainFindTab) {
            isLongClickable = false
            // api 26 以上 设置空text
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                tooltipText = ""
            }
            //和viewpager联动起来
            val titles = listOf(TYPE_ALL, TYPE_FOOD, TYPE_LIFE, TYPE_MEDICINE)
            TabLayoutMediator(this, mBinding.mainFindVp) { tab, position ->
                tab.text = titles[position]
            }.attach()
        }
    }

    private fun initVp() {
        val fragmentVpAdapter = FragmentVpAdapter(this)
        fragmentVpAdapter.add { ContentListFragment.newInstance(ContentType.MAIN) }
        fragmentVpAdapter.add { ContentListFragment.newInstance(ContentType.FOOD) }
        fragmentVpAdapter.add { ContentListFragment.newInstance(ContentType.LIFE) }
        fragmentVpAdapter.add { ContentListFragment.newInstance(ContentType.MEDICINE) }
        mBinding.mainFindVp.adapter = fragmentVpAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = FindFragment()

        const val TYPE_ALL = "全部"
        const val TYPE_FOOD = "食物"
        const val TYPE_LIFE = "生活"
        const val TYPE_MEDICINE = "药品"
    }
}