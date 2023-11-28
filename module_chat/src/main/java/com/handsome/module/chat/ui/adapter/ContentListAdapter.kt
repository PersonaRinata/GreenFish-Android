package com.handsome.module.chat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.handsome.module.chat.bean.SingleContentBean
import com.handsome.module.chat.databinding.ChatItemChatLeftBinding
import com.handsome.module.chat.databinding.ChatItemChatRightBinding

/**
 * 聊天的具体内容列表
 * @param ids 第一个参数指的是对面的id，第二个参数指的是自己的id
 */
class ContentListAdapter(private val ids : Pair<Long,Long>) : ListAdapter<SingleContentBean,ContentListAdapter.MyHolder>(diffUtil) {
    companion object{
        const val TYPE_LEFT = 1
        const val TYPE_RIGHT = 2
        val diffUtil = object : DiffUtil.ItemCallback<SingleContentBean>(){
            override fun areItemsTheSame(
                oldItem: SingleContentBean,
                newItem: SingleContentBean
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SingleContentBean,
                newItem: SingleContentBean
            ): Boolean {
                return  oldItem.content == newItem.content &&
                        oldItem.from_user_id == newItem.from_user_id &&
                        oldItem.to_user_id == newItem.to_user_id
            }
        }
    }


    sealed class MyHolder(binding : ViewBinding) : RecyclerView.ViewHolder(binding.root){
        abstract fun bind(data : SingleContentBean)
    }

    inner class LeftHolder(private val lBinding : ChatItemChatLeftBinding) : MyHolder(lBinding) {
        override fun bind(data: SingleContentBean) {
            lBinding.chatItemChatLeftTvMessage.text = data.content
        }
    }

    inner class RightHolder(private val rBinding  : ChatItemChatRightBinding) : MyHolder(rBinding) {
        override fun bind(data: SingleContentBean) {
            rBinding.chatItemChatRightTvMessage.text = data.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return when(viewType){
            TYPE_LEFT -> { LeftHolder(
                    ChatItemChatLeftBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_RIGHT -> { RightHolder(
                    ChatItemChatRightBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {throw Exception("消息解析异常")}
        }
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when(item.from_user_id){
            ids.first -> { TYPE_LEFT }
            ids.second -> { TYPE_RIGHT }
            else -> { throw Exception("消息异常") }
        }
    }
}