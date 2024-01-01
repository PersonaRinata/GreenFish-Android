package com.handsome.module.aigc.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.module.aigc.bean.AskAiBean
import com.handsome.module.aigc.bean.AskAiHistoryBean
import com.handsome.module.aigc.bean.RecommendDoctorBean
import com.handsome.module.aigc.net.AigcApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AigcViewModel : BaseViewModel() {

    private val _askQuestion = MutableLiveData<AskAiBean>()
    val askQuestion : LiveData<AskAiBean>
        get() = _askQuestion

    private val _aigcHistory = MutableLiveData<AskAiHistoryBean>()
    val aigcHistory : LiveData<AskAiHistoryBean>
        get() = _aigcHistory

    private val _recommendDoctor = MutableLiveData<Pair<String,RecommendDoctorBean>>()
    val recommendDoctor : LiveData<Pair<String,RecommendDoctorBean>>
        get() = _recommendDoctor

    fun askQuestion(content : String){
        AigcApiService.INSTANCE.askQuestion(content)
            .doOnError {
                Log.e("lx", "askQuestion:${it.message} ",it )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _askQuestion.postValue(it)
            }
    }

    fun aigcHistory(){
        AigcApiService.INSTANCE.getAskAiHistory()
            .doOnError {
                Log.e("lx", "askQuestion:${it.message} ",it )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _aigcHistory.postValue(it)
            }
    }

    fun recommendDoctor(content: String){
        AigcApiService.INSTANCE.getRecommendDoctor(content)
            .doOnError {
                Log.e("lx", "askQuestion:${it.message} ",it )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _recommendDoctor.postValue(content to it)
            }
    }

}