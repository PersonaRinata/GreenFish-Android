package com.handsome.yiqu.ui.viewmodel.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.yiqu.bean.ContentList
import com.handsome.yiqu.bean.ContentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContentListFragmentViewModel : ViewModel() {

    private val _mutableContentList = MutableStateFlow<ContentList?>(null)
    val contentList = _mutableContentList.asStateFlow()

    fun getContentList(type : ContentType){
//        ApiService.INSTANCE.getFindList(type.name)
        viewModelScope.launch(myCoroutineExceptionHandler){
            // todo 假数据生成地
            when(type){
                ContentType.MAIN -> {}
                ContentType.MEDICINE -> {}
                ContentType.FOOD -> {}
                ContentType.LIFE -> {}
                ContentType.HEALTHY -> {}
            }
            _mutableContentList.emit(fakeData)
        }
    }


    private val fakeData = ContentList(
        status_code = 0,
        status_msg = "",
        contentList = listOf(
            ContentList.SimpleContent(
                1,
                "",
                "猜猜我是谁",
                "2023.9.28",
            ),
            ContentList.SimpleContent(
                1,
                "",
                "原始人，启动！！！",
                "2023.9.27",
            ),
            ContentList.SimpleContent(
                1,
                "",
                "铂金这样做，王者这样做",
                "2023.9.26",
            ),
            ContentList.SimpleContent(
                1,
                "",
                "如何学好离散数学",
                "2023.9.25",
            ),
            ContentList.SimpleContent(
                1,
                "",
                "如何摆脱焦虑",
                "2023.9.24",
            ),
            ContentList.SimpleContent(
                1,
                "",
                "面试速通指南",
                "2023.9.23",
            ),
            ContentList.SimpleContent(
                1,
                "",
                "大学电脑选购指南",
                "2023.9.22",
            ),
            ContentList.SimpleContent(
                1,
                "",
                "如何评价王者新出英雄",
                "2023.9.21",
            ),
            ContentList.SimpleContent(
                1,
                "",
                "这有一份保研攻略请查收",
                "2023.9.20",
            ),
            ContentList.SimpleContent(
                1,
                "",
                "美味香喷喷的黄焖鸡米饭如何做的优雅又好吃",
                "2023.9.19",
            ), ContentList.SimpleContent(
                1,
                "",
                "200以内耳机推荐~",
                "2023.9.18",
            )

        )
    )

}