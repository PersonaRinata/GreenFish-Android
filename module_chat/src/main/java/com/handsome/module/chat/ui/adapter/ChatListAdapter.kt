package com.handsome.module.chat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.module.chat.bean.AuthorBean
import com.handsome.module.chat.databinding.ChatItemSingleFriendBinding

/**
 * @param selfId 参数的作用是跳转的时候传递进新的contentListActivity
 */
class ChatListAdapter(private val selfId : Long) : ListAdapter<AuthorBean,ChatListAdapter.MyHolder>(diffUtil) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<AuthorBean>(){
            override fun areItemsTheSame(oldItem: AuthorBean, newItem: AuthorBean): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AuthorBean, newItem: AuthorBean): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var mOnClickChatItem : ((selfId:Long,otherId:Long)->Unit)? = null

    fun setOnClickChatItem(onClickChatItem : (selfId:Long,otherId:Long)->Unit){
        this.mOnClickChatItem = onClickChatItem
    }

    inner class MyHolder(private val binding : ChatItemSingleFriendBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : AuthorBean){
            with(binding) {
                chatItemFriendsListTvName.text = data.name
                chatItemFriendsListTvMessage.text = data.message
            }
        }
        init {
            with(binding){
                chatItemFriendsListSlide.setOnTapTouchListener {
                    // 跳转到新的activity
                    mOnClickChatItem?.invoke(selfId,getItem(adapterPosition).id)
                }
                chatItemFriendsListIsTop.setOnClickListener{
                    // todo 置顶
                }
                chatItemFriendsListDelete.setOnClickListener {
                    //todo 删除
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ChatItemSingleFriendBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(getItem(position))
    }

}