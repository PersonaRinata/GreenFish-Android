package com.handsome.module.record.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.catchException
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.module.record.bean.ApiWrapperUserBean
import com.handsome.module.record.bean.IssueListBean
import com.handsome.module.record.net.RecordApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class RecordFragmentViewModel : BaseViewModel() {

    private val _issueList = MutableLiveData<IssueListBean>()
    val issueList : LiveData<IssueListBean>
        get() = _issueList


    private val _userInfo = MutableLiveData<ApiWrapperUserBean>()
    val userInfo : LiveData<ApiWrapperUserBean>
        get() = _userInfo


    fun getIssueList(userId : Long){
        RecordApiService.INSTANCE.getIssueList(userId)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
            _issueList.postValue(it)
        }
    }



    fun getUserInfo(userId : Long){
        RecordApiService.INSTANCE.getUserInfo(userId)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _userInfo.postValue(it)
            }
    }
}