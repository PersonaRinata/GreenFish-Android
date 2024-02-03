package com.handsome.module.publish.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.doPermissionAction
import com.handsome.lib.util.extention.getRequestBody
import com.handsome.lib.util.extention.gone
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.extention.visible
import com.handsome.module.publish.R
import com.handsome.module.publish.databinding.PublishActivityPublishBinding
import com.handsome.module.publish.ui.dialog.SuccessAndExitDialog
import com.handsome.module.publish.ui.viewmodel.PublishViewModel
import okhttp3.MultipartBody
import java.io.File
import java.io.IOException
import java.io.InputStream

class PublishActivity : BaseActivity() {
    private val mBinding by lazy { PublishActivityPublishBinding.inflate(layoutInflater) }
    private val videoFile by lazy { File(getExternalFilesDir(Environment.DIRECTORY_DCIM)?.absolutePath + File.separator + System.currentTimeMillis() + ".mp4") }
    private val mViewModel by viewModels<PublishViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initObserve()
        initClick()
    }

    private fun initObserve() {
        mViewModel.videoData.collectLaunch {
            if (it.isSuccess(this)){
                unLoading()
                SuccessAndExitDialog(this).apply {
                    setOnClickConfirm {
                        dismiss()
                        finishAfterTransition()
                    }
                }.show()
            }else{
                toast("上传失败，请重试！")
            }
        }
    }

    /**
     * 加载中，不能点击
     */
    private fun loading(){
        mBinding.publishPgBar.visible()
        mBinding.root.isClickable = false
    }

    private fun unLoading(){
        mBinding.publishPgBar.gone()
        mBinding.root.isClickable = true
    }

    private fun initClick() {
        mBinding.publishImgForeground.setOnClickListener {
            getVideoFromLocal()
        }
        mBinding.publishTvPublish.setOnClickListener {
            loading()
            val title = mBinding.publishEtDescribe.text.toString()
            uploadVideo(videoFile,title)
        }
        mBinding.publishImgBack.setOnClickListener {
            finishAfterTransition()
        }
    }

    private fun checkPermission(): Boolean {
        return checkSelfPermission(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
    }

    // 从本地获取视频，需要权限版本
    private fun getVideoFromLocal() {
        if (checkPermission()) {
            doPermissionAction(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
                reason = "读取视频需要访问您的存储空间哦~"  // 可选的。dialog 的内容
                doAfterGranted {
                    doAfterGranted()
                }
                doAfterRefused {
                    toast("无权访问存储空间")
                }
                doOnCancel {
                    toast("无权访问存储空间")
                }
            }
        } else {
            doAfterGranted()
        }
    }

    @SuppressLint("CheckResult")
    private fun doAfterGranted() {
        MaterialDialog(this@PublishActivity).show {
            listItems(items = listOf("拍照", "从相册中选择")) { dialog, index, text ->
                if (index == 0) {
                    getVideoFromCamera()
                } else {
                    getVideoFromAlbum()
                }
            }
            cornerRadius(res = com.handsome.lib.util.R.dimen.common_corner_radius)
        }
    }

    /**
     * 展示视频的第一帧作为封面
     */
    private fun showFirstBackground(uri: Uri) {
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // 在这里处理获取到的第一帧视频图片（即 Bitmap 对象）
                    // 例如，将 Bitmap 显示到 ImageView 中
                    mBinding.publishImgForeground.setImageBitmap(resource)
                    hintAdd()
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // 图像加载已被取消时执行
                    mBinding.publishImgForeground.setImageResource(R.drawable.publish_ic_picture_error)
                }
            })
    }

    // 当前界面已经有视频了，添加视频的img和提示文字隐藏
    private fun hintAdd(){
        with(mBinding) {
            publishActivityPublishLinearAdd.gone()
        }
    }

    /**
     * 跳转至视频选择界面
     */
    private val selectVideoFromAlbumLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            // 从 uri 获取视频文件，并进行后续操作
            if (uri != null) {
                createFileFromUri(uri)
                showFirstBackground(uri)
            } else {
                // 处理未选择视频文件的情况
                toast("未选择视频文件！")
            }
        }

    private val takeVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 处理录制的视频，可以在 result.data 中获取视频的 Uri
            val videoUri = result.data?.data
            videoUri?.let {
                createFileFromUri(it)
                showFirstBackground(it)
            }
            // 在这里执行接下来的操作
        } else {
            toast("录制视频失败")
        }
    }

    private fun uploadVideo(file: File,title : String) {
        try {
            val fileBody = MultipartBody.Part.createFormData(
                "data",
                file.name,
                file.getRequestBody()
            )
            mViewModel.uploadVideo(fileBody,title)
        } catch (e: IOException) {
            e.printStackTrace()
            unLoading()
            toast("视频加载失败")
        }
    }

    //文件权限在点击头像框时已经获取到了，这里不需要再获取
    private fun getVideoFromAlbum() {
        selectVideoFromAlbumLauncher.launch(arrayOf("video/*"))
    }

    // 相机拍摄的视频
    private fun getVideoFromCamera() {
        doPermissionAction(Manifest.permission.CAMERA) {
            reason = "拍照需要访问你的相机哦~"
            doAfterGranted {
                Log.d("lx", "被授权后调用 ")
                dispatchTakeVideoIntent()
            }
            doOnCancel {
                Log.d("lx", "权限被取消了 ")
            }
            doAfterRefused {
                Log.d("lx", "权限被拒绝了 ")
            }
        }
    }

    private fun createFileFromUri(uri: Uri) {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            videoFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakeVideoIntent() {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent.resolveActivity(packageManager) != null) {
            takeVideoLauncher.launch(takeVideoIntent)
        } else {
            // 没有适合的应用来处理 Intent
            toast("无法启动视频录制应用")
        }
    }



    companion object {
        fun startAction(context: Context) {
            val intent = Intent(context, PublishActivity::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

}