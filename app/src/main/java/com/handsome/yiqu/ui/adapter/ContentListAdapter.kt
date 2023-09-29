package com.handsome.yiqu.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.yiqu.bean.ContentList
import com.handsome.yiqu.databinding.MainItemContentListBinding

class ContentListAdapter : ListAdapter<ContentList.SimpleContent,ContentListAdapter.MyHolder>(myDiffUtil) {

    companion object{
        val myDiffUtil = object : DiffUtil.ItemCallback<ContentList.SimpleContent>() {
            override fun areItemsTheSame(oldItem: ContentList.SimpleContent, newItem: ContentList.SimpleContent): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ContentList.SimpleContent, newItem: ContentList.SimpleContent): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var mOnClickItem : ((id : Long) -> Unit)? = null

    fun setOnClickItem(onClickItem : (id : Long) -> Unit){
        this.mOnClickItem = onClickItem
    }

    inner class MyHolder(val binding : MainItemContentListBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.mainItemContentListConstrain.setOnClickListener {
                mOnClickItem?.invoke(getItem(adapterPosition).id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MainItemContentListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.binding.apply {
                if (item.pic != "")  mainItemContentListPic.setImageFromUrl(item.pic)
                mainItemContentListTitle.text = item.title
                mainItemContentListDate.text = item.date
            }
        }
    }


}