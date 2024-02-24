package com.handsome.module.aigc.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.handsome.module.aigc.R
import com.handsome.module.aigc.bean.DifferentModel
import com.handsome.module.aigc.bean.SingleModelBean
import com.handsome.module.aigc.databinding.AigcItemModelBinding

class DiffModelAdapter : ListAdapter<SingleModelBean, DiffModelAdapter.MyHolder>(myDiffUtil) {

    companion object {
        val myDiffUtil = object : DiffUtil.ItemCallback<SingleModelBean>() {
            override fun areItemsTheSame(
                oldItem: SingleModelBean,
                newItem: SingleModelBean
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SingleModelBean,
                newItem: SingleModelBean
            ): Boolean {
                return oldItem.isChoose == newItem.isChoose
            }
        }
    }

    private var mOnClickModel: ((DifferentModel) -> Unit)? = null

    fun setOnClickModel(onClickModel: (DifferentModel) -> Unit) {
        this.mOnClickModel = onClickModel
    }

    // 上一个点击的
    private var lastChooseIndex = 0

    inner class MyHolder(private val mBinding: AigcItemModelBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        init {
            mBinding.aigcItemModelFrameSingleModel.setOnClickListener {
                if (lastChooseIndex != adapterPosition){
                    mOnClickModel?.invoke(getItem(adapterPosition).enumModel)
                    val list = currentList.toMutableList()
                    list[lastChooseIndex].isChoose = false
                    list[adapterPosition].isChoose = true
                    notifyItemChanged(lastChooseIndex)
                    notifyItemChanged(adapterPosition)
                    lastChooseIndex = adapterPosition
                }
            }
        }

        fun bind(data: SingleModelBean) {
            with(mBinding) {
                aigcItemModelImgIcon.setImageResource(data.id)
                aigcItemModelTvName.text = data.enumModel.getChineseName()
                if (data.isChoose){
                    lastChooseIndex = adapterPosition
                    aigcItemModelFrameSingleModel.setBackgroundResource(R.drawable.aigc_shape_model_choose)
                    aigcItemModelTvName.setTextColor(mBinding.root.context.resources.getColor(R.color.aigc_model_choose_color,null))
                }else{
                    aigcItemModelFrameSingleModel.setBackgroundResource(R.drawable.aigc_shape_model_un_choose)
                    aigcItemModelTvName.setTextColor(mBinding.root.context.resources.getColor(com.handsome.lib.util.R.color.fish_font_level4_color,null))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            AigcItemModelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(getItem(position))
    }


}