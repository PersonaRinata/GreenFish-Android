package com.handsome.module.chat.ui.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.util.util.timeToDate
import com.handsome.module.chat.bean.IssueListBean
import com.handsome.module.chat.databinding.ChatItemDiseaseHistoryBinding

class DiseaseHistoryAdapter :
    ListAdapter<IssueListBean.IssueList.DiseaseRelation.HistoryDisease, DiseaseHistoryAdapter.MyHolder>(
        myDiffUtil
    ) {

    companion object {
        val myDiffUtil = object :
            DiffUtil.ItemCallback<IssueListBean.IssueList.DiseaseRelation.HistoryDisease>() {
            override fun areItemsTheSame(
                oldItem: IssueListBean.IssueList.DiseaseRelation.HistoryDisease,
                newItem: IssueListBean.IssueList.DiseaseRelation.HistoryDisease
            ): Boolean {
                return oldItem.update_time == newItem.update_time  // 看病时间一样我们认为这两个item是相同的
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: IssueListBean.IssueList.DiseaseRelation.HistoryDisease,
                newItem: IssueListBean.IssueList.DiseaseRelation.HistoryDisease
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MyHolder(private val mBinding: ChatItemDiseaseHistoryBinding) : RecyclerView.ViewHolder(mBinding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data : IssueListBean.IssueList.DiseaseRelation.HistoryDisease){
            mBinding.recordItemDiseaseHistoryResult.text = data.symptom
            mBinding.recordItemDiseaseHistoryTvHintDoctor.text = "张春生"
            val formattedDateTime = timeToDate(data.update_time)
            mBinding.recordItemDiseaseHistoryTvTime.text = formattedDateTime
            val sb = StringBuilder()
            data.medicines.forEach {
                sb.append(it).append("、")
            }
            mBinding.recordItemDiseaseHistoryTvMedicines.text = sb.substring(0,sb.length-1).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ChatItemDiseaseHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(getItem(position))
    }
}