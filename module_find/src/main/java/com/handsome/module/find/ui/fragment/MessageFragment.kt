package com.handsome.module.find.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseFragment
import com.handsome.module.find.R
import com.handsome.module.find.bean.ContentType
import com.handsome.module.find.databinding.FindFragmentMessageBinding
import com.handsome.module.find.databinding.FindTabMessageBinding


class MessageFragment : BaseFragment() {
    private val mBinding by lazy { FindFragmentMessageBinding.inflate(layoutInflater) }

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
        with(mBinding.findFragmentMessageTabLayout) {
            isLongClickable = false
            // api 26 以上 设置空text
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.O) {
                tooltipText = ""
            }

            val titles = listOf(TYPE_ALL, TYPE_FOOD, TYPE_LIFE, TYPE_MEDICINE)
            TabLayoutMediator(this, mBinding.findFragmentMessageVp2) { tab, position ->
                val binding = FindTabMessageBinding.inflate(layoutInflater)
                binding.findTabMessageTvLabel.text = titles[position]
                tab.customView = binding.root
            }.attach()
            setTabColor(getTabAt(0), R.color.find_message_tab_select_text_color)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    setTabColor(tab, R.color.find_message_tab_select_text_color)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    setTabColor(tab, R.color.find_message_tab_no_select_text_color)
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun setTabColor(tab: TabLayout.Tab?, @ColorRes id: Int) {
        val textView = tab?.customView?.findViewById<TextView>(R.id.find_tab_message_tv_label)
        textView?.setTextColor(resources.getColor(id, null))
    }

    private fun initVp() {
        val fragmentVpAdapter = FragmentVpAdapter(this)
        fragmentVpAdapter.add { MessagePageFragment.newInstance(ContentType.MAIN) }
        fragmentVpAdapter.add { MessagePageFragment.newInstance(ContentType.FOOD) }
        fragmentVpAdapter.add { MessagePageFragment.newInstance(ContentType.LIFE) }
        fragmentVpAdapter.add { MessagePageFragment.newInstance(ContentType.MEDICINE) }
        with(mBinding.findFragmentMessageVp2) {
            adapter = fragmentVpAdapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MessageFragment()

        const val TYPE_ALL = "全部"
        const val TYPE_FOOD = "食物"
        const val TYPE_LIFE = "生活"
        const val TYPE_MEDICINE = "药品"
    }
}