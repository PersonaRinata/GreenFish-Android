package com.handsome.module.aigc.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.handsome.module.aigc.bean.RecommendDoctorBean
import com.handsome.module.aigc.databinding.AigcItemMessageLeftBinding
import com.handsome.module.aigc.databinding.AigcItemMessageRightBinding
import com.handsome.module.aigc.databinding.AigcItemRecommendDoctorBinding

class MessageAdapter : ListAdapter<MessageAdapter.ContentListData,MessageAdapter.MyHolder>(myDiffUtil){
    companion object{
        const val TYPE_LEFT = 1
        const val TYPE_RIGHT = 2
        const val TYPE_RECOMMEND_DOCTOR = 3
        val myDiffUtil = object : DiffUtil.ItemCallback<ContentListData>() {
            override fun areItemsTheSame(oldItem: ContentListData, newItem: ContentListData): Boolean {
                return oldItem.getDataType() == newItem.getDataType()
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ContentListData, newItem: ContentListData): Boolean {
                return oldItem == newItem
            }
        }
    }

    sealed interface ContentListData{
        companion object{
            const val TYPE_LEFT_RIGHT = 1
            const val TYPE_RECOMMEND_DOCTOR = 2
        }
        fun getDataType() : Int
        data class TypeLeftRight(val singleContentBean: String) : ContentListData {
            override fun getDataType(): Int {
                return TYPE_LEFT_RIGHT
            }
        }

        data class TypeRecommendDoctor(val sumDiseaseBean: Pair<String,List<RecommendDoctorBean.Doctor>>) : ContentListData {
            override fun getDataType(): Int {
                return TYPE_RECOMMEND_DOCTOR
            }
        }
    }

    sealed class MyHolder(binding : ViewBinding) : RecyclerView.ViewHolder(binding.root){
        abstract fun bind(data : ContentListData)
    }

    inner class LeftHolder(private val lBinding : AigcItemMessageLeftBinding) : MyHolder(lBinding) {
        override fun bind(data: ContentListData) {
            if (data is ContentListData.TypeLeftRight){
                lBinding.aigcItemMessageLeftTvMessage.text = data.singleContentBean
            }
        }
    }

    inner class RightHolder(private val rBinding  : AigcItemMessageRightBinding) : MyHolder(rBinding) {
        override fun bind(data: ContentListData) {
            if (data is ContentListData.TypeLeftRight){
                rBinding.aigcItemMessageRightTvMessage.text = data.singleContentBean
            }
        }
    }

    private var mOnClickDoctor : ((RecommendDoctorBean.Doctor) -> Unit)? = null

    fun setOnClickDoctor(onClickDoctor : (RecommendDoctorBean.Doctor) -> Unit){
        this.mOnClickDoctor = onClickDoctor
    }

    inner class RecommendDoctorViewHolder(private val sBinding : AigcItemRecommendDoctorBinding) : MyHolder(sBinding){
        override fun bind(data: ContentListData) {
            if (data is ContentListData.TypeRecommendDoctor){
                sBinding.aigcItemRecommendDoctorTvIntroduction.text = data.sumDiseaseBean.first
                with(sBinding.aigcItemRecommendDoctorRvDoctors) {
                    layoutManager = GridLayoutManager(sBinding.root.context,3)
                    adapter = RecommendDoctorAdapter().apply {
                        submitList(data.sumDiseaseBean.second)
                        setOnClickDoctor {
                            mOnClickDoctor?.invoke(it)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return when(viewType){
            TYPE_LEFT -> {
                LeftHolder(AigcItemMessageLeftBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            TYPE_RIGHT -> {
                RightHolder(AigcItemMessageRightBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            TYPE_RECOMMEND_DOCTOR -> {
                RecommendDoctorViewHolder(AigcItemRecommendDoctorBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            else -> {throw Exception("消息异常")}
        }
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     *  奇数是自己，偶数是对面
     */
    private var messageNum = 0
    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is ContentListData.TypeLeftRight -> {
                messageNum++
                if(messageNum % 2 == 0){
                    TYPE_LEFT
                }else{
                    TYPE_RIGHT
                }
            }
            is ContentListData.TypeRecommendDoctor ->{
                TYPE_RECOMMEND_DOCTOR
            }
        }
    }
}