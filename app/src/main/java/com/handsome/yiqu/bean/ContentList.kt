package com.handsome.yiqu.bean

import java.io.Serializable

data class ContentList(
    val status_code: Int,
    val status_msg: String,
    val contentList : List<SimpleContent>,
) : Serializable{
    data class SimpleContent(
        val id : Long,  //内容的id
        val pic : String, // 内容的封面
        val title : String, // 信息概览
        val date : String, // 时间信息
//        val simpleContent : String //简单的信息
    ) : Serializable
}

enum class ContentType{
    MAIN,HEALTHY,FOOD,LIFE,MEDICINE
}
