package com.handsome.module.aigc.bean

import com.handsome.lib.util.network.IApiWrapper
import java.io.Serializable

data class RecommendDoctorBean(
    val department: String,
    val doctor_list: List<Doctor>,
    override val status_code: Int,
    override val status_msg: String
) : IApiWrapper {
    data class Doctor(
        val avatar: String,
        val background_image: String,
        val department: String,
        val favorite_count: Int,
        val follow_count: Int,
        val follower_count: Int,
        val id: Long,
        val is_follow: Boolean,
        val name: String,
        val signature: String,
        val total_favorited: Int,
        val work_count: Int
    ) : Serializable
}