package com.handsome.yiqu.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.yiqu.bean.FriendsList
import com.handsome.yiqu.databinding.MainItemFriendsListBinding

class FriendsListAdapter : ListAdapter<FriendsList.User,FriendsListAdapter.MyHolder>(myDiffUtil) {

    companion object{
        val myDiffUtil = object : DiffUtil.ItemCallback<FriendsList.User>() {
            override fun areItemsTheSame(oldItem: FriendsList.User, newItem: FriendsList.User): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: FriendsList.User, newItem: FriendsList.User): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var mOnClickItem : ((FriendsList.User) -> Unit)? = null

    fun setOnClickItem(onClickItem : (FriendsList.User) -> Unit){
        this.mOnClickItem = onClickItem
    }

    private var mOnClickTop : (() -> Unit)? = null

    fun setOnClickTop(mOnClickTop : () -> Unit){
        this.mOnClickTop = mOnClickTop
    }

    private var mOnClickDelete : (() -> Unit)? = null

    fun setOnClickDelete(mOnClickDelete : () -> Unit){
        this.mOnClickDelete = mOnClickDelete
    }

    inner class MyHolder(val binding : MainItemFriendsListBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.mainItemFriendsListSlide.setOnTapTouchListener {
                mOnClickItem?.invoke(getItem(adapterPosition))
            }
            binding.mainItemFriendsListIsTop.setOnClickListener {
                mOnClickTop?.invoke()
            }
            binding.mainItemFriendsListDelete.setOnClickListener {
                mOnClickDelete?.invoke()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MainItemFriendsListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.binding.apply {
                if (item.avatar != "") mainItemFriendsListImgUser.setImageFromUrl(item.avatar)
                mainItemFriendsListTvName.text = item.name
                mainItemFriendsListTvMessage.text = item.message
            }
        }
    }


}