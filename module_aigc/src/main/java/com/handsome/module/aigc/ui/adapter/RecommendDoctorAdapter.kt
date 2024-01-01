package com.handsome.module.aigc.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.handsome.lib.util.extention.setImageFromUrl
import com.handsome.module.aigc.bean.RecommendDoctorBean
import com.handsome.module.aigc.databinding.AigcItemDoctorBinding

class RecommendDoctorAdapter : ListAdapter<RecommendDoctorBean.Doctor,RecommendDoctorAdapter.MyHolder>(myDiffUtil) {
    companion object{
        val myDiffUtil = object : DiffUtil.ItemCallback<RecommendDoctorBean.Doctor>() {
            override fun areItemsTheSame(oldItem: RecommendDoctorBean.Doctor, newItem: RecommendDoctorBean.Doctor): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: RecommendDoctorBean.Doctor, newItem: RecommendDoctorBean.Doctor): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var mOnClickDoctor : ((RecommendDoctorBean.Doctor) -> Unit)? = null

    fun setOnClickDoctor(onClickDoctor : (RecommendDoctorBean.Doctor) -> Unit){
        this.mOnClickDoctor = onClickDoctor
    }
    
    inner class MyHolder(private val mBinding : AigcItemDoctorBinding) : ViewHolder(mBinding.root){
        init {
            mBinding.aigcItemDoctorLinear.setOnClickListener {
                mOnClickDoctor?.invoke(getItem(adapterPosition))
            }
        }
        fun bind(data : RecommendDoctorBean.Doctor){
            if (data.avatar != "") mBinding.aigcItemDoctorImgUser.setImageFromUrl(data.avatar)
            mBinding.aigcItemDoctorTvDoctorName.text = data.name
            mBinding.aigcItemDoctorTvDepartment.text = data.department
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(AigcItemDoctorBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(getItem(position))
    }
}