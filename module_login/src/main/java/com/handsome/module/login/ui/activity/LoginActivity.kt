package com.handsome.module.login.ui.activity

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.activity.viewModels
import com.handsome.lib.api.server.service.IMainService
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.setOnSingleClickListener
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.service.impl
import com.handsome.module.login.databinding.LoginActivityLoginBinding
import com.handsome.module.login.ui.viewmodel.LoginViewModel
import com.ndhzs.module.login.utils.textwatcher.BaseTextWatcher

//@Route(name = LOGIN_ENTRY, path = LOGIN_ENTRY)
class LoginActivity : BaseActivity() {
    private val mBinding by lazy { LoginActivityLoginBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initView()
        initClick()
        initEvent()
        initObserve()
    }

    private fun initClick() {
        mBinding.loginCbRemember.setOnCheckedChangeListener { _, isChecked ->
            mViewModel.changeRememberPassword(isChecked)
        }
        // 使用 setOnSingleClickListener 防抖 (即 500 毫秒内的多次点击无效)
        mBinding.loginBtnLogin.setOnSingleClickListener {
            val username = mBinding.loginEtUsername.text?.toString()
            val password = mBinding.loginEtPassword.text?.toString()
            if (username.isNullOrBlank() || password.isNullOrBlank()) {
                toast("请输入完整!")
            } else {
                mViewModel.login(username, password)
            }
        }
        mBinding.loginBtnRegister.setOnSingleClickListener {
            val username = mBinding.loginEtUsername.text?.toString()
            val password = mBinding.loginEtPassword.text?.toString()
            if (username.isNullOrBlank() || password.isNullOrBlank()) {
                toast("请输入完整!")
            } else {
                mViewModel.register(username, password,password)
            }
        }
    }

    private fun initEvent() {
        mBinding.loginEtUsername.addTextChangedListener(BaseTextWatcher(mBinding.loginTilUsername))
        mBinding.loginEtPassword.addTextChangedListener(BaseTextWatcher(mBinding.loginTilPassword))
    }

    private fun initView() {
        mBinding.loginCbRemember.isChecked = mViewModel.isRememberPassword()
    }

    private fun initObserve() {
        mViewModel.username.collectLaunch {
            mBinding.loginEtUsername.setText(it)
        }
        mViewModel.password.collectLaunch{
            mBinding.loginEtPassword.setText(it)
        }
        mViewModel.loginEvent.collectLaunch {
            when(it){
                is LoginViewModel.LoginEvent.Success -> {
                    IMainService::class.impl.startMainActivity()
                    finishAfterTransition() // 其他页面可能返回动画，所以使用这个方法
                }
                is LoginViewModel.LoginEvent.Fail -> {
                    toast(it.error.message)
                }
            }
        }

    }

    companion object{
        fun startAction(context : Context){
            val intent = Intent(context,LoginActivity::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}