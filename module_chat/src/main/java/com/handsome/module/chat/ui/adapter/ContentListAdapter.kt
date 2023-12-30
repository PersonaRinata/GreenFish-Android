package com.handsome.module.chat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.handsome.module.chat.bean.IssueListBean
import com.handsome.module.chat.bean.SingleContentBean
import com.handsome.module.chat.bean.SumDiseaseBean
import com.handsome.module.chat.databinding.ChatItemChatLeftBinding
import com.handsome.module.chat.databinding.ChatItemChatRightBinding
import com.handsome.module.chat.databinding.ChatItemIssueListBinding
import com.handsome.module.chat.databinding.ChatItemSumDiseaseBinding
import com.handsome.module.chat.wight.selectable.SelectableTextViewHelper

/**
 * 聊天的具体内容列表
 * @param ids 第一个参数指的是对面的id，第二个参数指的是自己的id
 */
class ContentListAdapter(private val ids : Pair<Long,Long>) : ListAdapter<ContentListAdapter.ContentListData,ContentListAdapter.MyHolder>(diffUtil) {
    companion object{
        const val TYPE_LEFT = 1
        const val TYPE_RIGHT = 2
        const val TYPE_SUM_DISEASE = 3
        const val TYPE_ISSUE_LIST = 4
        val diffUtil = object : DiffUtil.ItemCallback<ContentListData>(){
            override fun areItemsTheSame(
                oldItem: ContentListData,
                newItem: ContentListData
            ): Boolean {
                return oldItem.getDataType() == newItem.getDataType()
            }

            override fun areContentsTheSame(
                oldItem: ContentListData,
                newItem: ContentListData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    sealed interface ContentListData{
        companion object{
            const val TYPE_LEFT_RIGHT = 1
            const val TYPE_SUM_DISEASE = 2
            const val TYPE_ISSUE_LIST = 3
        }
        fun getDataType() : Int
        data class TypeLeftRight(val singleContentBean: SingleContentBean) : ContentListData {
            override fun getDataType(): Int {
                return TYPE_LEFT_RIGHT
            }
        }

        data class TypeSumDisease(val sumDiseaseBean: SumDiseaseBean) : ContentListData {
            override fun getDataType(): Int {
                return TYPE_SUM_DISEASE
            }
        }

        data class TypeIssueList(val issueListBean: IssueListBean) : ContentListData {
            override fun getDataType(): Int {
                return TYPE_ISSUE_LIST
            }
        }
    }

    sealed class MyHolder(binding : ViewBinding) : RecyclerView.ViewHolder(binding.root){
        abstract fun bind(data : ContentListData)
    }

    inner class LeftHolder(private val lBinding : ChatItemChatLeftBinding) : MyHolder(lBinding) {
        init {
            SelectableTextViewHelper(lBinding.chatItemChatLeftTvMessage)
        }
        override fun bind(data: ContentListData) {
            if (data is ContentListData.TypeLeftRight){
                lBinding.chatItemChatLeftTvMessage.text = data.singleContentBean.content
            }
        }
    }

    inner class RightHolder(private val rBinding  : ChatItemChatRightBinding) : MyHolder(rBinding) {
        init {
            SelectableTextViewHelper(rBinding.chatItemChatRightTvMessage)
        }
        override fun bind(data: ContentListData) {
            if (data is ContentListData.TypeLeftRight){
                rBinding.chatItemChatRightTvMessage.text = data.singleContentBean.content
            }
        }
    }

    inner class SumDiseaseViewHolder(private val sBinding : ChatItemSumDiseaseBinding) : MyHolder(sBinding){
        override fun bind(data: ContentListData) {
            if (data is ContentListData.TypeSumDisease){
                sBinding.chatItemSumDiseaseTvIntroduction.text = data.sumDiseaseBean.msg
            }
        }

    }

    inner class IssueListViewHolder(private val iBinding : ChatItemIssueListBinding) : MyHolder(iBinding){
        override fun bind(data: ContentListData) {
            if (data is ContentListData.TypeIssueList){
                val issueList = data.issueListBean.issue_list
                with(iBinding) {
                    chatItemIssueListTvName.text = issueList.username
                    chatItemIssueListTvAge.text = issueList.age.toString()
                    chatItemIssueListTvGender.text = if(issueList.gender) "男" else "女"

                    val bodyInfo = issueList.body_info
                    bodyInfo?.let {
                        chatItemIssueListTvHeight.text = it.height
                        chatItemIssueListTvWeight.text = it.weight
                        chatItemIssueListTvBloodSugar.text = it.blood_sugar
                        chatItemIssueListTvBloodPressure.text = it.blood_pressure
                        chatItemIssueListTvHeartRate.text = it.heart_rate
                    }
                    val diseaseRelation =  issueList.disease_relation
                    diseaseRelation?.let {
                        chatItemIssueListTvFamilyDisease.text = it.family_diseases
                        chatItemIssueListTvSelfIntroduction.text =  it.disease_introduction
                        chatItemIssueListRvDiseaseHistory.layoutManager = LinearLayoutManager(iBinding.root.context)
                        chatItemIssueListRvDiseaseHistory.adapter = DiseaseHistoryAdapter().apply{submitList(it.history_diseases)}
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return when(viewType){
            TYPE_LEFT -> { LeftHolder(
                    ChatItemChatLeftBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_RIGHT -> { RightHolder(
                    ChatItemChatRightBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_SUM_DISEASE -> {
                SumDiseaseViewHolder(
                    ChatItemSumDiseaseBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_ISSUE_LIST -> {
                IssueListViewHolder(
                    ChatItemIssueListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {throw Exception("消息解析异常")}
        }
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when(val item = getItem(position)){
            is ContentListData.TypeLeftRight -> {
                val content = item.singleContentBean
                when (content.from_user_id) {
                    ids.first -> { TYPE_LEFT }
                    ids.second -> { TYPE_RIGHT }
                    else -> { throw Exception("消息异常") }
                }
            }
            is ContentListData.TypeIssueList -> { TYPE_ISSUE_LIST }
            is ContentListData.TypeSumDisease -> { TYPE_SUM_DISEASE }
        }
    }
}