package com.handsome.module.chat.bean

import com.handsome.lib.util.network.IApiWrapper
import java.io.Serializable

data class SingleContentBean(
    val id : Long,  //消息id
    val to_user_id : Long, //接收者id
    val from_user_id : Long, //传递者id
    val content : String,
    val create_time : Long
) : Serializable

data class ContentListBean(
    override val status_code : Int,
    override val status_msg: String,
    val message_list : List<SingleContentBean>?
) : IApiWrapper