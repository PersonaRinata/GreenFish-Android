package com.handsome.module.mine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.api.video.bean.AuthorBean
import com.handsome.lib.util.extention.invisible
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.lib.util.extention.visible
import com.handsome.module.mine.R
import com.handsome.module.mine.databinding.MineItemFollowBinding

class FollowAdapter : ListAdapter<AuthorBean, FollowAdapter.MyHolder>(myDiffUtil) {

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


    inner class MyHolder(val binding: MineItemFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {
                mineItemFriendsListImgUser.setOnClickListener {
                    mOnClickUser?.invoke(getItem(adapterPosition))
                }
                mineItemFriendsListTvName.setOnClickListener {
                    mOnClickUser?.invoke(getItem(adapterPosition))
                }
                mineItemFollowTvFollow.setOnClickListener {
                    mOnClickFollow?.invoke(getItem(adapterPosition))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            MineItemFollowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            if (item.avatar!="") mineItemFriendsListImgUser.setImageFromUrl(item.avatar)
            mineItemFriendsListTvName.text = item.nickname
            with(mineItemFollowTvFollow) {
                if (item.is_follow) {
                    text = "已关注"
                    setBackgroundResource(R.drawable.mine_shape_list_have_follow_bg)
                    setTextColor(resources.getColor(R.color.white,null))
                } else {
                    text = "关注"
                    setBackgroundResource(R.drawable.mine_shape_list_follow_bg)
                    setTextColor(resources.getColor(R.color.mine_follow_text_color,null))
                }
            }
            if (item.department != ""){
                mineItemFriendsListImgOfficial.visible()
                mineItemFriendsListTvMessage.visible()
                mineItemFriendsListTvFans.invisible()
            }else{
                mineItemFriendsListImgOfficial.invisible()
                mineItemFriendsListTvMessage.invisible()
                val text = "粉丝数：${item.follower_count}"
                mineItemFriendsListTvFans.text = text
                mineItemFriendsListTvFans.visible()
            }
        }
    }
}