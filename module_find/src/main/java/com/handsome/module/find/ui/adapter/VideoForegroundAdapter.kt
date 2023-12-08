package com.handsome.module.find.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.api.video.bean.VideoBean
import com.handsome.lib.util.extention.setImageFromId
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.module.find.R
import com.handsome.module.find.databinding.FindItemVideoForegroudBinding

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

    inner class MyHolder(val binding : FindItemVideoForegroudBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                mOnClickVideo?.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(FindItemVideoForegroudBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.binding.apply {
                if (item.cover_url != "") findItemVideoForegroundImgForeground.setImageFromUrl(item.cover_url,R.drawable.find_ic_place_foreground_photo)
                if (item.author.avatar != "") findItemVideoForegroundImgUser.setImageFromUrl(item.author.avatar)
                findItemVideoForegroundTvLikeNum.text = item.favorite_count.toString()
                findItemVideoForegroundTvUsername.text = item.author.name
                findItemVideoForegroundTvTitle.text = item.title
                if (item.is_favorite) {
                    findItemVideoForegroundImgLike.setImageFromId(R.drawable.find_ic_like)
                }else{
                    findItemVideoForegroundImgLike.setImageFromId(R.drawable.find_ic_unlike)
                }
            }
        }
    }

}