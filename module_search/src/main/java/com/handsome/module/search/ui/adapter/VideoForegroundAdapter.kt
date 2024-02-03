package com.handsome.module.search.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.handsome.api.video.bean.VideoBean
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.module.search.R
import com.handsome.module.search.databinding.SearchItemVideoForegroudBinding

class VideoForegroundAdapter : PagingDataAdapter<VideoBean,VideoForegroundAdapter.MyHolder>(myDiffUtil) {

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

    inner class MyHolder(val binding : SearchItemVideoForegroudBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { it1 -> mOnClickVideo?.invoke(it1) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(SearchItemVideoForegroudBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.binding.apply {
                if (item.cover_url != "") searchItemVideoForegroundImgForeground.setImageFromUrl(item.cover_url,R.drawable.search_ic_place_foreground_photo,R.drawable.search_ic_place_foreground_photo)
                searchItemVideoForegroundTvTitle.text = item.title
            }
        }
    }
}