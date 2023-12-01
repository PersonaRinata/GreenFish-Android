package com.handsome.module.mine.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.api.video.bean.VideoBean
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.module.mine.databinding.MineItemVideoForegroudBinding

class VideoForegroundAdapter : ListAdapter<VideoBean,VideoForegroundAdapter.MyHolder>(myDiffUtil) {

    companion object{
        val myDiffUtil = object : DiffUtil.ItemCallback<VideoBean>() {
            override fun areItemsTheSame(oldItem: VideoBean, newItem: VideoBean): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: VideoBean, newItem: VideoBean): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var mOnClickVideo : ((VideoBean) -> Unit)? = null

    fun setOnClickVideo(mOnClickVideo : (VideoBean) -> Unit){
        this.mOnClickVideo = mOnClickVideo
    }

    inner class MyHolder(val binding : MineItemVideoForegroudBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                mOnClickVideo?.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MineItemVideoForegroudBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.binding.apply {
                if (item.cover_url != "") mainItemVideoForegroudImgUser.setImageFromUrl(item.cover_url)
                mainItemVideoForegroudTvLikeNum.text = item.favorite_count.toString()
            }
        }
    }

}