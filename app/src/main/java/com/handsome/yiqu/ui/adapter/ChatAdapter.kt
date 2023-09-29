package com.handsome.yiqu.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.handsome.lib.util.extention.toast
import com.handsome.yiqu.bean.ChatMessageBean
import com.handsome.yiqu.databinding.MainItemChatLeftBinding
import com.handsome.yiqu.databinding.MainItemChatRightBinding

class ChatAdapter(private val selfId: Long) :
    ListAdapter<ChatMessageBean, ChatAdapter.MyHolder>(myDiffUtil) {

    companion object {
        val myDiffUtil = object : DiffUtil.ItemCallback<ChatMessageBean>() {
            override fun areItemsTheSame(
                oldItem: ChatMessageBean,
                newItem: ChatMessageBean
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ChatMessageBean,
                newItem: ChatMessageBean
            ): Boolean {
                return oldItem == newItem
            }
        }
        const val LEFT = 1
        const val RIGHT = 2
    }

    sealed class MyHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {}

    inner class MyLeftHolder(val binding: MainItemChatLeftBinding) : MyHolder(binding) {
        init {
            // 左边的头像的点击事件
            toast("跳转到用户界面")
        }
    }

    inner class MyRightHolder(val binding: MainItemChatRightBinding) : MyHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return if (viewType == RIGHT){
            MyRightHolder(MainItemChatRightBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }else if (viewType == LEFT){
            MyLeftHolder(MainItemChatLeftBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }else{
            throw Exception("类型不明确异常")
        }
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        Log.d("lx","(ChatAdapter.kt:61)-->>currentList${currentList}");
        Log.d("lx", "onBindViewHolder:item ${item} ")
        when(holder){
            is MyLeftHolder -> {
                with(holder.binding) {
                    // todo 等待到时候传进来头像
//                    mainItemChatLeftImgUser.setImageFromUrl()
                    mainItemChatLeftTvMessage.text = item.content
                }
            }
            is MyRightHolder -> {
                with(holder.binding){
//                    mainItemChatRightImgUser.setImageFromUrl()
                    mainItemChatRightTvMessage.text = item.content
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return if (selfId == item.from_user_id) RIGHT else LEFT
    }
}