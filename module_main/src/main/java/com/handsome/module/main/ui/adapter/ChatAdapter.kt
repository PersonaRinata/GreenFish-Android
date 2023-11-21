package com.handsome.module.main.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.handsome.module.main.databinding.MainItemChatLeftBinding
import com.handsome.module.main.databinding.MainItemChatRightBinding

class ChatAdapter(private val selfId: Long) :
    ListAdapter<com.handsome.module.main.bean.ChatMessageBean, ChatAdapter.MyHolder>(myDiffUtil) {

    companion object {
        val myDiffUtil = object : DiffUtil.ItemCallback<com.handsome.module.main.bean.ChatMessageBean>() {
            override fun areItemsTheSame(
                oldItem: com.handsome.module.main.bean.ChatMessageBean,
                newItem: com.handsome.module.main.bean.ChatMessageBean
            ): Boolean {
                return oldItem.create_time == newItem.create_time
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: com.handsome.module.main.bean.ChatMessageBean,
                newItem: com.handsome.module.main.bean.ChatMessageBean
            ): Boolean {
                return oldItem == newItem
            }
        }
        const val LEFT = 1
        const val RIGHT = 2
    }

    private var mClickUser : ((com.handsome.module.main.bean.ChatMessageBean) -> Unit)? = null

    fun setOnClickUser(mClickUser : (com.handsome.module.main.bean.ChatMessageBean) -> Unit){
        this.mClickUser = mClickUser
    }

    sealed class MyHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    inner class MyLeftHolder(val binding: MainItemChatLeftBinding) : MyHolder(binding) {
        init {
            // 左边的头像的点击事件
            binding.mainItemChatLeftImgUser.setOnClickListener {
                mClickUser?.invoke(getItem(adapterPosition))
            }
        }
    }

    inner class MyRightHolder(val binding: MainItemChatRightBinding) : MyHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return when (viewType) {
            RIGHT -> {
                MyRightHolder(MainItemChatRightBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            LEFT -> {
                MyLeftHolder(MainItemChatLeftBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            else -> {
                throw Exception("类型不明确异常")
            }
        }
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        Log.d("lx","(ChatAdapter.kt:74)-->>currentList${currentList}")
        Log.d("lx", "onBindViewHolder:item $item ")
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