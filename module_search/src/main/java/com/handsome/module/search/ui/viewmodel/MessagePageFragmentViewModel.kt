package com.handsome.module.search.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handsome.module.search.bean.ContentList
import com.handsome.module.search.ui.fragment.MessagePageFragment.Companion.TYPE_ALL
import com.handsome.module.search.ui.fragment.MessagePageFragment.Companion.TYPE_FOOD
import com.handsome.module.search.ui.fragment.MessagePageFragment.Companion.TYPE_LIFE
import com.handsome.module.search.ui.fragment.MessagePageFragment.Companion.TYPE_MEDICINE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MessagePageFragmentViewModel : ViewModel() {

    private val _mutableContentList = MutableStateFlow<ContentList?>(null)
    val contentList: StateFlow<ContentList?>
        get() = _mutableContentList.asStateFlow()

    fun getContentList(content : String) {
//        ApiService.INSTANCE.getFindList(type.name)

        viewModelScope.launch {
            // 本地非模糊匹配
            val contentList = testData.contentList.filter { it.title.contains(content) }
            _mutableContentList.emit(ContentList(
                status_code = 0,
                status_msg = "",
                contentList
            ))
        }
    }


    private val testData = ContentList(
        status_code = 0,
        status_msg = "",
        contentList = listOf(
            ContentList.SimpleContent(
                1,
                "",
                "软组织损伤应该如何做",
                "2023.9.28",
                listOf(TYPE_ALL),
                "https://zhuanlan.zhihu.com/p/47070974"
            ),
            ContentList.SimpleContent(
                1,
                "",
                "肝脏真的会在睡着之后偷偷解毒吗？",
                "2023.9.27",
                listOf(TYPE_LIFE),
                "https://www.zhihu.com/question/292326555"
            ),
            ContentList.SimpleContent(
                1,
                "",
                "这有一份气血养身茶指南，请查收",
                "2023.9.26",
                listOf(TYPE_LIFE),
                "https://zhuanlan.zhihu.com/p/355887990"
            ),
            ContentList.SimpleContent(
                1,
                "",
                "红花油和云南白药哪个更好用一点",
                "2023.9.25",
                listOf(TYPE_MEDICINE),
                "https://www.zhihu.com/question/277629191/answer/395819216"
            ),
            ContentList.SimpleContent(
                1,
                "",
                "上火了该吃点什么缓解",
                "2023.9.24",
                listOf(TYPE_LIFE, TYPE_FOOD),
                "https://www.zhihu.com/question/555088419/answer/2689655399"

            ),
            ContentList.SimpleContent(
                1,
                "",
                "每天一杯牛奶真的会有益于健康吗？",
                "2023.9.23",
                listOf(TYPE_FOOD),
                "https://zhuanlan.zhihu.com/p/543124130"
            ),
            ContentList.SimpleContent(
                1,
                "",
                "眼睛痛该用什么药呢？",
                "2023.9.22",
                listOf(TYPE_MEDICINE),
                "https://zhuanlan.zhihu.com/p/385780609"
            ),
            ContentList.SimpleContent(
                1,
                "",
                "布洛芬颗粒是干什么的？",
                "2023.9.21",
                listOf(TYPE_MEDICINE),
                "https://www.zhihu.com/question/565921115"
            ),
            ContentList.SimpleContent(
                1,
                "",
                "口腔溃疡喷剂推荐",
                "2023.9.20",
                listOf(TYPE_MEDICINE),
                "https://zhuanlan.zhihu.com/p/580051963"
            ),
            ContentList.SimpleContent(
                1,
                "",
                "古代常说的食疗真的有效吗？是如何生效的？",
                "2023.9.19",
                listOf(TYPE_LIFE, TYPE_FOOD),
                "https://www.zhihu.com/question/27275791/answer/70859332"
            ),
            ContentList.SimpleContent(
                1,
                "",
                "该如何增强免疫力？",
                "2023.9.18",
                listOf(TYPE_LIFE),
                "https://www.zhihu.com/market/paid_magazine/1237848577964969984/section/1237848635561123840?origin_label=search"
            )

        )
    )

}