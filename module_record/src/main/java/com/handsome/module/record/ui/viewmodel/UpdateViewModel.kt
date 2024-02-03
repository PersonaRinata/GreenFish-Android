package com.handsome.module.record.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.catchException
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.lib.util.network.ApiStatus
import com.handsome.module.record.bean.UpdateIssueListBean
import com.handsome.module.record.net.RecordApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class UpdateViewModel : BaseViewModel() {

    private val _updateIssueList = MutableLiveData<ApiStatus>()
    val updateIssueList : LiveData<ApiStatus>
        get() = _updateIssueList

    fun updateIssueList(updateIssueListBean: UpdateIssueListBean,userId : Long){
        RecordApiService.INSTANCE.updateIssueList(updateIssueListBean,userId)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _updateIssueList.postValue(it)
            }
    }
}