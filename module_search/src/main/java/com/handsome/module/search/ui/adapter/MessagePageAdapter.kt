package com.handsome.module.search.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromId
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.module.search.R
import com.handsome.module.search.bean.ContentList
import com.handsome.module.search.databinding.SearchItemMessagePageBinding

class MessagePageAdapter :
    ListAdapter<ContentList.SimpleContent, MessagePageAdapter.MyHolder>(myDiffUtil) {

    companion object {
        val myDiffUtil = object : DiffUtil.ItemCallback<ContentList.SimpleContent>() {
            override fun areItemsTheSame(
                oldItem: ContentList.SimpleContent,
                newItem: ContentList.SimpleContent
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ContentList.SimpleContent,
                newItem: ContentList.SimpleContent
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var mOnClickItem: ((content: ContentList.SimpleContent) -> Unit)? = null

    fun setOnClickItem(onClickItem: (content: ContentList.SimpleContent) -> Unit) {
        this.mOnClickItem = onClickItem
    }

    inner class MyHolder(val binding: SearchItemMessagePageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.mainItemContentListConstrain.setOnClickListener {
                mOnClickItem?.invoke(getItem(bindingAdapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            SearchItemMessagePageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.binding.apply {
                if (item.pic != "") {
                    searchItemMessagePageImgPic.setImageFromUrl(item.pic)
                } else {
                    searchItemMessagePageImgPic.setImageFromId(R.drawable.search_ic_place_message)
                }
                searchItemMessagePageTvTitle.text = item.title
                searchItemMessagePageTvDate.text = item.date
            }
        }
    }


}