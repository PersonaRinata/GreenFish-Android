package com.handsome.module.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.handsome.api.video.bean.AuthorBean
import com.handsome.lib.util.extention.gone
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.visible
import com.handsome.module.search.R
import com.handsome.module.search.databinding.SearchItemFollowBinding

class FollowAdapter : PagingDataAdapter<AuthorBean, FollowAdapter.MyHolder>(myDiffUtil) {

    companion object {
        val myDiffUtil = object : DiffUtil.ItemCallback<AuthorBean>() {
            override fun areItemsTheSame(
                oldItem: AuthorBean,
                newItem: AuthorBean
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AuthorBean,
                newItem: AuthorBean
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var mOnClickUser: ((AuthorBean) -> Unit)? = null
    fun setOnClickUser(mOnClickUser: (AuthorBean) -> Unit) {
        this.mOnClickUser = mOnClickUser
    }

    private var mOnClickFollow: ((AuthorBean) -> Unit)? = null
    fun setOnClickFollow(mOnClickFollow: (AuthorBean) -> Unit) {
        this.mOnClickFollow = mOnClickFollow
    }


    inner class MyHolder(val binding: SearchItemFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {
                mineItemFriendsListImgUser.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { it1 -> mOnClickUser?.invoke(it1) }
                }
                mineItemFriendsListTvName.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { it1 -> mOnClickUser?.invoke(it1) }
                }
                mineItemFollowTvFollow.setOnClickListener {
                    getItem(bindingAdapterPosition)?.let { it1 -> mOnClickFollow?.invoke(it1) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            SearchItemFollowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position) ?: return
        with(holder.binding) {
            if (item.avatar!="") mineItemFriendsListImgUser.setImageFromUrl(item.avatar)
            mineItemFriendsListTvName.text = item.nickname
            with(mineItemFollowTvFollow) {
                if (item.is_follow) {
                    text = "已关注"
                    setBackgroundResource(R.drawable.search_shape_list_have_follow_bg)
                    setTextColor(resources.getColor(R.color.white,null))
                } else {
                    text = "关注"
                    setBackgroundResource(R.drawable.search_shape_list_follow_bg)
                    setTextColor(resources.getColor(R.color.search_follow_text_color,null))
                }
            }
            if (item.department != ""){
                mineItemFriendsListImgOfficial.visible()
                mineItemFriendsListTvMessage.visible()
                mineItemFriendsListTvFans.gone()
            }else{
                mineItemFriendsListImgOfficial.gone()
                mineItemFriendsListTvMessage.gone()
                val text = "粉丝数：${item.follower_count}"
                mineItemFriendsListTvFans.text = text
                mineItemFriendsListTvFans.visible()
            }
        }
    }
}