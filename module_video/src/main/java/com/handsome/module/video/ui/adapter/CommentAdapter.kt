package com.handsome.module.video.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.module.search.databinding.VideoItemCommentBinding
import com.handsome.module.video.bean.CommentBean

class CommentAdapter : ListAdapter<CommentBean.Comment,CommentAdapter.MyHolder>(myDiffUtil) {


    companion object{
        val myDiffUtil = object : DiffUtil.ItemCallback<CommentBean.Comment>() {
            override fun areItemsTheSame(oldItem: CommentBean.Comment, newItem: CommentBean.Comment): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: CommentBean.Comment, newItem: CommentBean.Comment): Boolean {
                return oldItem == newItem
            }
        }
    }

    /**
     * 点击头像操作
     */
    private var mOnClickUser : ((userId : Long) -> Unit)? = null

    fun setOnClickUser(onClickUser : (userId : Long) -> Unit){
        this.mOnClickUser = onClickUser
    }


    inner class MyHolder(val binding : VideoItemCommentBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.videoItemCommentImgUser.setOnClickListener {
                mOnClickUser?.invoke(getItem(adapterPosition).user.id)
            }
            binding.videoItemCommentTvUserName.setOnClickListener {
                mOnClickUser?.invoke(getItem(adapterPosition).user.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(VideoItemCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.binding.apply {
                if (item.user.avatar != "") videoItemCommentImgUser.setImageFromUrl(item.user.avatar)
                videoItemCommentTvUserName.text = item.user.name
                videoItemCommentTvComment.text = item.content
                videoItemCommentTvTime.text = item.create_date
            }
        }
    }

}