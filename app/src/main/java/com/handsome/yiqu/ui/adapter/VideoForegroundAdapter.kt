package com.handsome.yiqu.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.toast
import com.handsome.yiqu.bean.VideoBean
import com.handsome.yiqu.databinding.MainItemVideoForegroudBinding

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
    
    inner class MyHolder(val binding : MainItemVideoForegroudBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            //todo 设置点击事件
            binding.root.setOnClickListener {
                //todo 跳转到播放界面
                toast("改跳转了，快去做")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MainItemVideoForegroudBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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