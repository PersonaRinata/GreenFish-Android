package com.handsome.module.chat.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.module.chat.bean.AuthorBean
import com.handsome.module.chat.databinding.ChatItemSingleFriendBinding
import com.handsome.module.chat.wight.OnSlideChangedListener
import com.handsome.module.chat.wight.SlideMenuLayout

/**
 * @param selfId 参数的作用是跳转的时候传递进新的contentListActivity
 */
class ChatListAdapter(private val selfId : Long) : ListAdapter<AuthorBean,ChatListAdapter.MyHolder>(diffUtil) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<AuthorBean>(){
            override fun areItemsTheSame(oldItem: AuthorBean, newItem: AuthorBean): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AuthorBean, newItem: AuthorBean): Boolean {
                return oldItem == newItem
            }
        }
    }

    //当前右滑打开的位置
    var rightSlideOpenLoc : Int? = null

    private var mOnItemSlideBack : ((loc : Int) -> Unit)? = null

    /**
     * 设置上一个滑动的item的closeRight
     */
    fun setOnItemSlideBack(func : (loc : Int) -> Unit){
        this.mOnItemSlideBack = func
    }

    private var mOnClickChatItem : ((selfId:Long,otherId:Long)->Unit)? = null

    fun setOnClickChatItem(onClickChatItem : (selfId:Long,otherId:Long)->Unit){
        this.mOnClickChatItem = onClickChatItem
    }

    private var mOnClickDelete : ((AuthorBean)->Unit)? = null

    fun setOnClickDelete(onClickDelete : (AuthorBean)->Unit){
        this.mOnClickDelete = onClickDelete
    }


    private var mOnClickTop : ((AuthorBean,TextView)->Unit)? = null

    fun setOnClickTop(onClickTop : (AuthorBean,TextView)->Unit){
        this.mOnClickTop = onClickTop
    }

    inner class MyHolder(private val binding : ChatItemSingleFriendBinding) : RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data : AuthorBean){
            with(binding) {
                chatItemFriendsListTvName.text = data.name
                chatItemFriendsListTvMessage.text = data.message
                chatItemFriendsListIsTop.text =if (data.isTop) "取消置顶" else "置顶"
                if (data.isOpen){
                    chatItemFriendsListSlide.openRightSlide()
                }else{
                    chatItemFriendsListSlide.closeRightSlide()
                }
//                chatItemFriendsListTvTime.text = data
            }
        }
        init {
            with(binding){
                chatItemFriendsListSlide.setOnTapTouchListener {
                    // 跳转到新的activity
                    mOnClickChatItem?.invoke(selfId,getItem(adapterPosition).id)
                }
                chatItemFriendsListIsTop.setOnClickListener{
                    mOnClickTop?.invoke(getItem(adapterPosition),binding.chatItemFriendsListIsTop)
                }
                chatItemFriendsListDelete.setOnClickListener {
                    mOnClickDelete?.invoke(getItem(adapterPosition))
                }
                chatItemFriendsListSlide.setOnSlideChangedListener(object : OnSlideChangedListener {
                    override fun onSlideStateChanged(
                        slideMenu: SlideMenuLayout,
                        isLeftSlideOpen: Boolean,
                        isRightSlideOpen: Boolean
                    ) {
                        if (isRightSlideOpen){
                            // 在滑动另外一个之前先把其它打开的关闭
                            rightSlideOpenLoc?.let {
                                if (it != adapterPosition){
                                    mOnItemSlideBack?.invoke(it)
                                }
                            }
                            rightSlideOpenLoc = adapterPosition
                        }
                    }
                    override fun onSlideRightChanged(percent: Float) {}

                    override fun onSlideLeftChanged(percent: Float) {}

                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ChatItemSingleFriendBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun deleteItem(authorBean: AuthorBean){
        val list = currentList.toMutableList()
        list.remove(authorBean)
        submitList(list)
    }

    /**
     * 将传进来的列表变成有序列表，上面是置顶
     */
    fun submitListToOrder(authors: List<AuthorBean>){
        val orderList = ArrayList<AuthorBean>()
        authors.forEach {
            if (it.isTop){
                orderList.add(0,it)
            }else{
                orderList.add(it)
            }
        }
        submitList(orderList)
    }

    /**
     * 远端更新成功之后就调用，将该条数据按照是否有序插入并且提交，相比于submitListToOrder减少了比较
     * 因为没有接口，所以暂时做的本地
     */
    fun addItemToOrder(item : AuthorBean, tvIsGroup : TextView){
        val list = currentList.toMutableList()
        if (item.isTop){
            // 当前是置顶状态，需要改成取消置顶状态，显示文字为置顶
            tvIsGroup.text = "置顶"
            list.remove(item)
            //取消置顶就放到最后
            list.add(item)  //置顶效果
        }else{
            tvIsGroup.text = "取消置顶"
            list.remove(item)
            //置顶就放到第一个
            list.add(0, item)  //置顶效果
        }
        submitList(list)
    }

}