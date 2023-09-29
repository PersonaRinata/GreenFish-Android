package com.handsome.yiqu.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.setImageFromLocalUri
import com.handsome.lib.util.extention.toast
import com.handsome.yiqu.databinding.MainActivityPublishBinding
import com.handsome.yiqu.ui.viewmodel.activity.PublishActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.IOException


class PublishActivity : BaseActivity() {
    private val mBinding by lazy { MainActivityPublishBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<PublishActivityViewModel>()

    private var mVideo: MultipartBody.Part? = null

    private val pickVideo =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                Log.d("lx", "uri:${uri} ")
                mBinding.mainActivityPublishImgForeground.setImageFromLocalUri(uri)
                val inputStream = applicationContext.contentResolver.openInputStream(uri)
                inputStream?.let { stream ->
                    // 创建 RequestBody
                    // 创建 RequestBody
                    val requestBody: RequestBody = object : RequestBody() {
                        override fun contentType(): MediaType? {
                            return "video/*".toMediaTypeOrNull()
                        }

                        @Throws(IOException::class)
                        override fun writeTo(sink: BufferedSink) {
                            // 从输入流中读取数据并写入到请求体
                            val buffer = ByteArray(4096)
                            var bytesRead: Int
                            while (stream.read(buffer).also { bytesRead = it } != -1) {
                                sink.write(buffer, 0, bytesRead)
                            }
                        }
                    }
                    mVideo = MultipartBody.Part.createFormData("data","data",requestBody)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initObserve()
        initClick()
    }


    private fun initObserve() {
        lifecycleScope.launch {
            mViewModel.publishVideo.collectLatest {
                if (it != null) {
                    Log.d("lx", "initObserve: it = $it")
                    finish()
                }
            }
        }
    }

    /**
     * 初始化点击事件
     */
    private fun initClick() {
        // 初始化image的点击事件，也就是发布视频
        mBinding.mainActivityPublishImgForeground.apply {
            setOnClickListener {
                pickVideo.launch(arrayOf("video/*"))
            }
        }
        mBinding.mainActivityPublishBtnPublish.apply {
            setOnClickListener {
                val title = mBinding.mainActivityPublishEtDescribe.text.toString()
                if (mVideo != null) {
                    mViewModel.publishVideo(mVideo!!, title)
                } else {
                    toast("还没有上传视频哦~")
                }
            }
        }
    }

    companion object {
        fun startAction(context: Context) {
            val intent = Intent(context, PublishActivity::class.java)
            context.startActivity(intent)
        }
    }
}