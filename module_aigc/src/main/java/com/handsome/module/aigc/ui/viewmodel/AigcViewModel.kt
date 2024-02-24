package com.handsome.module.aigc.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handsome.lib.util.base.BaseViewModel
import com.handsome.lib.util.extention.catchException
import com.handsome.lib.util.extention.unsafeSubscribeBy
import com.handsome.module.aigc.bean.AskAiBean
import com.handsome.module.aigc.bean.AskAiHistoryBean
import com.handsome.module.aigc.bean.DifferentModel
import com.handsome.module.aigc.bean.JudgeDoctorBean
import com.handsome.module.aigc.bean.ModelChatBean
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

    private val _isDoctor = MutableLiveData<JudgeDoctorBean>()
    val isDoctor : LiveData<JudgeDoctorBean>
        get() = _isDoctor


    private val _doctorModel = MutableLiveData(DifferentModel.COMMON)
    val doctorModel : LiveData<DifferentModel>
        get() = _doctorModel

    private val _askModelQuestion = MutableLiveData<Pair<String,ModelChatBean>>()
    val askModelQuestion : LiveData<Pair<String,ModelChatBean>>
        get() = _askModelQuestion

    fun setDoctorModel(differentModel: DifferentModel){
        _doctorModel.value = differentModel
    }

    fun askModelQuestion(category: String,query : String){
        AigcApiService.INSTANCE.postModelChat(query,category)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _askModelQuestion.postValue(query to it)
            }
    }

    fun askQuestion(content : String){
        AigcApiService.INSTANCE.askQuestion(content)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _askQuestion.postValue(it)
            }
    }

    fun aigcHistory(){
        AigcApiService.INSTANCE.getAskAiHistory()
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _aigcHistory.postValue(it)
            }
    }

    fun recommendDoctor(content: String){
        AigcApiService.INSTANCE.getRecommendDoctor(content)
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _recommendDoctor.postValue(content to it)
            }
    }

    fun isDoctor(){
        AigcApiService.INSTANCE.judgeDoctor()
            .catchException {}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsafeSubscribeBy {
                _isDoctor.postValue(it)
            }
    }

}