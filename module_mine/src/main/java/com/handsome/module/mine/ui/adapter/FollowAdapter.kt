package com.handsome.module.mine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.api.video.bean.AuthorBean
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

    private var mOnClickFollow: ((AuthorBean, textView: TextView) -> Unit)? = null
    fun setOnClickFollow(mOnClickFollow: (AuthorBean, textView: TextView) -> Unit) {
        this.mOnClickFollow = mOnClickFollow
    }


    inner class MyHolder(val binding: MineItemFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {
                mainItemFollowImgUser.setOnClickListener {
                    mOnClickUser?.invoke(getItem(adapterPosition))
                }
                mainItemFollowTvName.setOnClickListener {
                    mOnClickUser?.invoke(getItem(adapterPosition))
                }
                mainItemFollowTvFollow.setOnClickListener {
                    mOnClickFollow?.invoke(getItem(adapterPosition),it as TextView)
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
//            mainItemFollowImgUser.setImageFromUrl()
            mainItemFollowTvName.text = item.name
            with(mainItemFollowTvFollow) {
                if (item.is_follow) {
                    text = "已关注"
                    setBackgroundResource(R.drawable.mine_shape_follow_have_bg)
                } else {
                    text = "关注"
                    setBackgroundResource(R.drawable.mine_shape_follow_no_bg)
                }
            }

        }
    }
}