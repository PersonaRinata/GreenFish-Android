package com.handsome.module.mine.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.google.android.material.tabs.TabLayoutMediator
import com.handsome.api.video.bean.AuthorBean
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.api.server.service.IChatService
import com.handsome.lib.api.server.service.IPublishService
import com.handsome.lib.api.server.service.IRecordService
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseFragment
import com.handsome.lib.util.extention.doPermissionAction
import com.handsome.lib.util.extention.getRequestBody
import com.handsome.lib.util.extention.gone
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.setOnSingleClickListener
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.extention.uri
import com.handsome.lib.util.extention.visible
import com.handsome.lib.util.service.impl
import com.handsome.module.mine.R
import com.handsome.module.mine.databinding.MineFragmentMineBinding
import com.handsome.module.mine.ui.activity.FollowListActivity
import com.handsome.module.mine.ui.viewmodel.MineViewModel
import com.yalantis.ucrop.UCrop
import okhttp3.MultipartBody
import java.io.File
import java.io.IOException

// 后续考虑通过service
class MineFragment : BaseFragment() {
    private val mBinding by lazy { MineFragmentMineBinding.inflate(layoutInflater) }
    private val mCurrentUserId by arguments<Long>()   // 这个是获得当前user的id，不一定是自己
    private val mUserInfo by lazy { IAccountService::class.impl.getUserInfo() }  //这个是获得用户本人的信息，一定是自己
    private var mCurrentUserInfo: AuthorBean? = null
    private val mViewModel by viewModels<MineViewModel>()
    private var mIsFollow = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObserve()
        initView()
        return mBinding.root
    }

    private fun initView() {
        initVpTab()
        initClick()
        initVisible()
    }

    // 初始化关注聊天和病例的可见度的方法
    private fun initVisible() {
        if (mCurrentUserId == mUserInfo?.user_id) {
            // 证明是本人，显示病例
            mBinding.mineFragmentMineConstrainHealthRecord.visible()
            mBinding.mineFragmentMineLinearFollowAndChat.gone()
        } else {
            // 不是本人，访问他人，显示关注按钮和私聊按钮
            mBinding.mineFragmentMineConstrainHealthRecord.gone()
            mBinding.mineFragmentMineLinearFollowAndChat.visible()

        }
    }

    private fun initClick() {
        with(mBinding) {
            mineFragmentMineLinearFansText.setOnClickListener {
                FollowListActivity.startAction(
                    requireContext(),
                    mCurrentUserId,
                    FollowListActivity.FollowType.FANS
                )
            }
            mineFragmentMineLinearFollowText.setOnClickListener {
                FollowListActivity.startAction(
                    requireContext(),
                    mCurrentUserId,
                    FollowListActivity.FollowType.FOLLOWS
                )
            }
            mineFragmentMineImgUser.setOnClickListener {
                // 更换头像
                getImgFromLocal()
            }
            mineFragmentMineFloatBtnPublish.setOnClickListener {
                IPublishService::class.impl.startPublishActivity()
            }
            mineFragmentMineTvFollow.setOnSingleClickListener {
                mCurrentUserInfo?.let {
                    mViewModel.followUser(mCurrentUserId, mIsFollow)
                }
            }
            mineFragmentMineConstrainHealthRecord.setOnClickListener {
                // 跳转到病例界面
                IRecordService::class.impl.startRecordActivity()
            }
            mineFragmentMineLinearPrivateChat.setOnClickListener {
                mUserInfo?.let {
                    mCurrentUserInfo?.let { it1 ->
                        IChatService::class.impl.startContentListActivity(it.user_id, mCurrentUserId, it1.name)
                    }
                }
            }
        }
    }


    private fun initVpTab() {
        initVp()
        initTabLayout()
    }

    private fun initVp() {
        // tab + vp
        val fragmentVpAdapter = FragmentVpAdapter(this).apply {
            add {
                VideoFlowFragment.newInstance(
                    VideoFlowFragment.MineType.TYPE_PUBLISH,
                    mCurrentUserId
                )
            }
            add {
                VideoFlowFragment.newInstance(
                    VideoFlowFragment.MineType.TYPE_LIKE,
                    mCurrentUserId
                )
            }
        }
        with(mBinding.mineFragmentMineVp) {
            adapter = fragmentVpAdapter
            // 禁止用户自己滑动
            isUserInputEnabled = false
        }
        initTabLayout()
    }

    private fun initTabLayout() {
        // 取消tabLayout的长按点击事件
        with(mBinding.mineFragmentMineTabLayout) {
            isLongClickable = false
            // api 26 以上 设置空text
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.O) {
                tooltipText = ""
            }
            val titles = listOf("发布", "喜欢")
            //和viewpager联动起来
            TabLayoutMediator(this, mBinding.mineFragmentMineVp) { tab, position ->
                tab.text = titles[position]
            }.attach()
        }
    }

    override fun onStart() {
        super.onStart()
        getUserInfo()
    }

    private fun getUserInfo() {
        mViewModel.getUserInfo(mCurrentUserId)
    }

    private fun initObserve() {
        mViewModel.userInfo.collectLaunch {
            if (it != null) {
                if (it.status_code == 0) {
                    val userInfo = it.user
                    mCurrentUserInfo = userInfo
                    with(mBinding) {
                        mineFragmentMineTvUserName.text = userInfo.name
                        if (userInfo.avatar != "") mineFragmentMineImgUser.setImageFromUrl(userInfo.name)
                        mineFragmentMineTvDescribe.text = userInfo.signature
                        mineFragmentMineTvFollowNum.text = userInfo.follow_count.toString()
                        mineFragmentMineTvFansNum.text = userInfo.follower_count.toString()
                        if (userInfo.department != "") {
                            mBinding.mineFragmentMineLinearOfficial.visible()
                        } else {
                            mBinding.mineFragmentMineLinearOfficial.visible()
                        }
                        mIsFollow = userInfo.is_follow
                    }
                } else {
                    toast("网络错误")
                }
            }
        }
        mViewModel.uploadImg.collectLaunch {
            if (it != null) {
                if (it.status_code == 0) {
                    toast("头像更换成功")
                } else {
                    toast("网络错误")
                }
            }
        }
        mViewModel.followUser.collectLaunch {
            if (it != null) {
                if (it.status_code == 0) {
                    mIsFollow = !mIsFollow
                    setFollowBg(mIsFollow)
                } else {
                    toast("网络错误")
                }
            }
        }
    }

    private fun setFollowBg(isFollow: Boolean) {
        with(mBinding.mineFragmentMineTvFollow) {
            text = if (isFollow) { // 如果是关注操作，则显示已经关注
                setBackgroundResource(R.drawable.mine_shape_follow_have_bg)
                "已关注"
            } else {  // 如果是取消关注操作
                setBackgroundResource(R.drawable.mine_shape_follow_bg)
                "关注"
            }
        }
    }

    /**
     * 下面是上传图片的操作
     */
    private val cameraImageFile by lazy { File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM)?.absolutePath + File.separator + System.currentTimeMillis() + ".png") }
    private val destinationFile by lazy { File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM)?.absolutePath + File.separator + mCurrentUserId + ".png") }

    private fun checkPermission(): Boolean {
        return requireActivity().checkSelfPermission(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                && requireActivity().checkSelfPermission(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
    }

    private fun getImgFromLocal() {
        if (checkPermission()) {
            doPermissionAction(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
                reason = "读取照片需要访问您的存储空间哦~"  // 可选的。dialog 的内容
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
        //选择
        MaterialDialog(requireContext()).show {
            listItems(items = listOf("拍照", "从相册中选择")) { dialog, index, text ->
                if (index == 0) {
                    getImageFromCamera()
                } else {
                    getImageFromAlbum()
                }
            }
            cornerRadius(res = com.handsome.lib.util.R.dimen.common_corner_radius)
        }
    }


    /**
     * 用于跳转至拍照界面获取头像
     */
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            //若获取成功则进入裁剪界面
            if (result) {
                startCropActivity(cameraImageFile.uri)
            }
        }

    private fun startCropActivity(uri: Uri) {
        val uCrop = UCrop.of(uri, Uri.fromFile(destinationFile))
        val options = UCrop.Options()
        options.setCropGridStrokeWidth(5)
        options.setCompressionFormat(Bitmap.CompressFormat.PNG)
        options.setCompressionQuality(100)
        options.setLogoColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.teal_200
            )
        )
        options.setToolbarColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        // 不显示网格线
        options.setShowCropGrid(false)
        options.setStatusBarColor(
            ContextCompat.getColor(requireContext(), R.color.teal_200)
        )
        options.setCircleDimmedLayer(true)
        uCrop.withOptions(options)
            .withAspectRatio(300f, 300f)
            .withMaxResultSize(300, 300)
            .start(requireContext(), this)
    }

    /**
     * 申请权限后跳转至拍照界面获取图片
     */
    private fun getImageFromCamera() {
        doPermissionAction(Manifest.permission.CAMERA) {
            reason = "拍照需要访问你的相机哦~"
            doAfterGranted {
                takePictureLauncher.launch(cameraImageFile.uri)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("lx", "状态码 requestCode = $requestCode ")

        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == UCrop.REQUEST_CROP) {
            Log.d("lx", "onActivityResult: 我刚裁剪回来")
            if (data != null) {
                uploadImage(data)
            } else {
                toast("未知错误，请重试")
            }
        }
    }

    /**
     * 跳转至图片选择界面，选择图片用于裁剪后上传头像
     */
    private val selectPictureFromAlbumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val uri = it.data?.data
            if (uri != null) {
                startCropActivity(uri)
            } else {
                toast("无法识别该图像")
            }
        }

    //文件权限在点击头像框时已经获取到了，这里不需要再获取
    private fun getImageFromAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
        selectPictureFromAlbumLauncher.launch(intent)
    }

    private fun uploadImage(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri == null) {
            toast("无法获得裁剪结果")
            return
        }

        try {
            val fileBody = MultipartBody.Part.createFormData(
                "avatar",
                destinationFile.name,
                destinationFile.getRequestBody()
            )
            mViewModel.uploadImg(fileBody)
        } catch (e: IOException) {
            e.printStackTrace()
            toast("图片加载失败")
        }
    }


    /**
     * 必须通过newInstance来调用，通过不同的userId来达到不同的效果
     */
    companion object {
        fun newInstance(userId: Long) = MineFragment().apply {
            arguments = bundleOf(
                this::mCurrentUserId.name to userId
            )
        }
    }
}