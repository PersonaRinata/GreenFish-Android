package com.handsome.module.find.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.toast
import com.handsome.module.find.databinding.FindActivityWebViewBinding

class WebActivity : BaseActivity() {
    private val mBinding by lazy { FindActivityWebViewBinding.inflate(layoutInflater) }
    private var mUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initValue()
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        try {
            mBinding.findActivityWebWevView.apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                if (mUrl != null) {
                    loadUrl(mUrl!!)
                } else {
                    "加载失败".toast()
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
            "加载失败".toast()
        }

    }

    private fun initValue() {
        mUrl = intent.getStringExtra("url").toString()
    }


    companion object{
        fun startAction(context : Context,url : String){
            val intent = Intent(context,WebActivity::class.java)
            intent.putExtra("url",url)
            context.startActivity(intent)
        }
    }
}