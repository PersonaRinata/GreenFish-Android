package com.handsome.module.main.ui.activity

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.activity.viewModels
import com.handsome.lib.api.server.MAIN_CHAT
import com.handsome.lib.api.server.MAIN_FIND
import com.handsome.lib.api.server.MAIN_PERSONALIZATION
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.api.server.service.ILoginService
import com.handsome.lib.api.server.service.IMineService
import com.handsome.lib.api.server.service.IPublishService
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.service.ServiceManager
import com.handsome.lib.util.service.impl
import com.handsome.module.main.R
import com.handsome.module.main.databinding.MainActivityMainBinding
import com.handsome.module.main.ui.viewmodel.activity.MainActivityViewModel

class MainActivity : BaseActivity() {
    private val mBinding by lazy { MainActivityMainBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<MainActivityViewModel>()
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
        val mUserInfo = IAccountService::class.impl.getUserInfo()
        val fragmentVpAdapter = FragmentVpAdapter(this)
        fragmentVpAdapter
            .add{ServiceManager.fragment(MAIN_FIND)}
            .add{ServiceManager.fragment(MAIN_PERSONALIZATION)}
            .add{ServiceManager.fragment(MAIN_CHAT)}
            .add{IMineService::class.impl.getMineFragment(mUserInfo!!.user_id)}
        mBinding.mainVp.adapter = fragmentVpAdapter
        mBinding.mainVp.isUserInputEnabled = false;  //禁止滑动的方法
    }

    private fun initBottomNavi() {
        mBinding.mainNavi.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_main_bottom_recommend -> {
                    mBinding.mainVp.currentItem = 0
                }

                R.id.item_main_bottom_pre_judgment -> {
                    mBinding.mainVp.currentItem = 1
                }

                R.id.item_main_bottom_publish -> {
                    IPublishService::class.impl.startPublishActivity()
                }

                R.id.item_main_bottom_chat -> {
                    mBinding.mainVp.currentItem = 2
                }

                R.id.item_main_bottom_mine -> {
                    mBinding.mainVp.currentItem = 3
                }
            }
            return@setOnItemSelectedListener true
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