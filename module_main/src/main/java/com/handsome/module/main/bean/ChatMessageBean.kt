package com.handsome.module.main.bean

data class ChatMessageBean(
    val id : Long,  //消息id
    val to_user_id : Long, //接收者id
    val from_user_id : Long, //传递者id
    val content : String,
    val create_time : Long
)

data class ApiWrapperChatMessageBean(
    val status_code : Int,
    val status_msg: String,
    val message_list : List<com.handsome.module.main.bean.ChatMessageBean>?
)
