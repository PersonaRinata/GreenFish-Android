package com.handsome.yiqu.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.toast
import com.handsome.yiqu.bean.LoginBean
import com.handsome.yiqu.databinding.MainActivityLoginBinding
import com.handsome.yiqu.net.ApiService
import com.handsome.yiqu.ui.viewmodel.activity.LoginActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    private val mBinding by lazy { MainActivityLoginBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<LoginActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initObserve()
        initLogin()
        initRegister()
    }

    private fun initObserve() {
        lifecycleScope.launch {
            mViewModel.loginIn.collectLatest {
                if (it != null){
                    if (it.status_code == 0){
                        saveUserInfo(it)
                        getUserInfo()
                    }else{
                        toast("用户不存在")
                    }
                }
            }
        }
        lifecycleScope.launch {
            mViewModel.registerIn.collectLatest {
                if (it != null){
                    if (it.status_code == 0){
                        // 注册成功
                        toast("注册成功，请登录")
                    }else{
                        toast("网络异常")
                    }
                }
            }
        }
        lifecycleScope.launch {
            mViewModel.userInfo.collectLatest {
                if (it != null) {
                    if (it.status_code == 0) {
                        // 登录成功
                        Log.d("lx", "loginActivity observe userInfo ${it}: ")
                        MainActivity.startAction(this@LoginActivity,it.user)
                        finishAfterTransition()
                    } else {
                        saveUserInfo(LoginBean(0,"","",0L))
                        toast("网络错误")
                    }
                }
            }
        }
    }

    private fun getUserInfo() {
        mViewModel.getUserInfo(ApiService.userId)
    }

    private fun initRegister() {
        // 注册的操作
        with(mBinding){
            mainActivityLoginBtnRegister.setOnClickListener {
                val userPair = getEtInfo()
                mViewModel.registerIn(userPair.first,userPair.second)
            }
        }
    }

    private fun initLogin() {
        // 登录的操作
        with(mBinding){
            mainActivityLoginBtnLogin.setOnClickListener {
                val userPair = getEtInfo()
                mViewModel.loginIn(userPair.first,userPair.second)
            }
        }
    }

    private fun getEtInfo() : Pair<String,String>{
        with(mBinding){
            val userName = mainActivityLoginEtUserName.text.toString()
            val password = mainActivityLoginEtPassWord.text.toString()
            return userName to password
        }
    }

    private fun saveUserInfo(loginBean : LoginBean){
        with(ApiService) {
            token = loginBean.token
            userId = loginBean.user_id
        }
    }

}