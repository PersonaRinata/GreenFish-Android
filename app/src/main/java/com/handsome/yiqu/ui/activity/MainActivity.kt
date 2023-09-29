package com.handsome.yiqu.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseActivity
import com.handsome.yiqu.R
import com.handsome.yiqu.databinding.MainActivityMainBinding
import com.handsome.yiqu.ui.fragment.ChatFragment
import com.handsome.yiqu.ui.fragment.MineFragment
import com.handsome.yiqu.ui.fragment.FindFragment
import com.handsome.yiqu.ui.fragment.RecommendFragment

class MainActivity : BaseActivity() {
    private val mBinding by lazy { MainActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initVp()
        initBottomNavi()        //初始话导航栏的方法
    }

    private fun initVp() {
        val fragmentVpAdapter = FragmentVpAdapter(this)
        fragmentVpAdapter
            .add(RecommendFragment::class.java)
            .add(FindFragment::class.java)
            .add(ChatFragment::class.java)
            .add(MineFragment::class.java)
        mBinding.mainVp.adapter = fragmentVpAdapter
        mBinding.mainVp.isUserInputEnabled = false;  //禁止滑动的方法
    }

    private fun initBottomNavi() {
        mBinding.mainNavi.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_main_bottom_recommend -> {mBinding.mainVp.currentItem = 0}
                R.id.item_main_bottom_pre_judgment -> {mBinding.mainVp.currentItem = 1}
                R.id.item_main_bottom_publish -> { PublishActivity.startAction(this) }
                R.id.item_main_bottom_chat -> {mBinding.mainVp.currentItem = 2}
                R.id.item_main_bottom_mine -> {mBinding.mainVp.currentItem = 3}
            }
            return@setOnItemSelectedListener true
        }
    }

    companion object{
        fun startAction(context: Context){
            val intent = Intent(context,MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}