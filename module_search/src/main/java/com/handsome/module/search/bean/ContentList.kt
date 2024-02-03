package com.handsome.module.search.bean

import com.handsome.lib.util.network.IApiWrapper
import java.io.Serializable

data class ContentList(
    override val status_code: Int,
    override val status_msg: String,
    val contentList : List<SimpleContent>,
) : IApiWrapper {
    data class SimpleContent(
        val id : Long,  //内容的id
        val pic : String, // 内容的封面
        val title : String, // 信息概览
        val date : String, // 时间信息
        val types : List<String>,
//        val simpleContent : String //简单的信息
        val url : String
    ) : Serializable
}