package com.handsome.module.main

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import com.handsome.lib.api.server.MAIN_AIGC
import com.handsome.lib.api.server.MAIN_CHAT
import com.handsome.lib.api.server.MAIN_FIND
import com.handsome.lib.api.server.MAIN_MINE
import com.handsome.lib.api.server.MAIN_RECORD
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.api.server.service.ILoginService
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.service.ServiceManager
import com.handsome.lib.util.service.impl
import com.handsome.module.main.databinding.MainActivityMainBinding

class MainActivity : BaseActivity() {
    private val mBinding by lazy { MainActivityMainBinding.inflate(layoutInflater) }
    private var isLogin : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLogin = IAccountService::class.impl.isLogin()
        if (!isLogin){
            ILoginService::class.impl.startLoginActivity()
            finish()
        }
        initUi()
    }

    private fun initUi() {
        setContentView(mBinding.root)
        initVp()
        initBottomNavi()        //初始话导航栏的方法
    }

    private fun initVp() {
        val fragmentVpAdapter = FragmentVpAdapter(this)
        fragmentVpAdapter
            .add{ServiceManager.fragment(MAIN_FIND)}
            .add{ServiceManager.fragment(MAIN_CHAT)}
            .add{ServiceManager.fragment(MAIN_AIGC)}
            .add{ServiceManager.fragment(MAIN_RECORD)}
            .add{ServiceManager.fragment(MAIN_MINE)}
        mBinding.mainVp.adapter = fragmentVpAdapter
        mBinding.mainVp.isUserInputEnabled = false;  //禁止滑动的方法
    }

    private fun initBottomNavi() {
        // 上一个点击的下标
        var lastClickIndex = 0
        with(mBinding) {
            mainBottomLinearFind.setOnClickListener {
                performNavi(0,lastClickIndex)
                lastClickIndex = 0
            }
            mainBottomLinearChat.setOnClickListener {
                performNavi(1,lastClickIndex)
                lastClickIndex = 1
            }
            mainBottomLinearGreenFish.setOnClickListener {
                performNavi(2,lastClickIndex)
                lastClickIndex = 2
            }
            mainBottomLinearHealthRecord.setOnClickListener {
                performNavi(3,lastClickIndex)
                lastClickIndex = 3
            }
            mainBottomLinearMine.setOnClickListener {
                performNavi(4,lastClickIndex)
                lastClickIndex = 4
            }
        }
    }

    private fun performNavi(currentIndex : Int,lastIndex : Int){
        if (currentIndex == lastIndex) return
        mBinding.mainVp.setCurrentItem(currentIndex,false)
        when(lastIndex){
            0 -> { mBinding.mainBottomImgFind.setImageResource(R.drawable.main_ic_find) }
            1 -> { mBinding.mainBottomImgChat.setImageResource(R.drawable.main_ic_chat) }
            2 -> { mBinding.mainBottomImgGreenFish.setImageResource(R.drawable.main_ic_aigc) }
            3 -> { mBinding.mainBottomImgHealthRecord.setImageResource(R.drawable.main_ic_health_record) }
            4 -> { mBinding.mainBottomImgMine.setImageResource(R.drawable.main_ic_mine) }
        }
        when(currentIndex){
            0 -> { mBinding.mainBottomImgFind.setImageResource(R.drawable.main_ic_find_select) }
            1 -> { mBinding.mainBottomImgChat.setImageResource(R.drawable.main_ic_chat_select) }
            2 -> { mBinding.mainBottomImgGreenFish.setImageResource(R.drawable.main_ic_aigc_select) }
            3 -> { mBinding.mainBottomImgHealthRecord.setImageResource(R.drawable.main_ic_health_record_select) }
            4 -> { mBinding.mainBottomImgMine.setImageResource(R.drawable.main_ic_mine_select) }
        }
    }

    companion object {
        fun startAction(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}