package com.handsome.module.aigc.bean

import androidx.annotation.DrawableRes

data class SingleModelBean(
    @DrawableRes val id : Int,
    val enumModel : DifferentModel,
    var isChoose : Boolean
)
