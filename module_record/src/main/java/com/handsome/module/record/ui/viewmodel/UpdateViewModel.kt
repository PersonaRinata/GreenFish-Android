package com.handsome.module.record.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.module.record.bean.StatusBean
import com.handsome.module.record.bean.UpdateIssueListBean
import com.handsome.module.record.net.RecordApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class UpdateViewModel : BaseViewModel() {

    private val _updateIssueList = MutableLiveData<StatusBean>()
    val updateIssueList : LiveData<StatusBean>
        get() = _updateIssueList

    fun updateIssueList(updateIssueListBean: UpdateIssueListBean,userId : Long){
        RecordApiService.INSTANCE.updateIssueList(updateIssueListBean,userId).doOnError {
            Log.e("lx","(RecordFragmentViewModel.kt:19)-->>${it.message}",it)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _updateIssueList.postValue(it)
            }
    }
}