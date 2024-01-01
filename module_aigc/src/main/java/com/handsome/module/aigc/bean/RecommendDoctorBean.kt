package com.handsome.module.aigc.bean

data class RecommendDoctorBean(
    val department: String,
    val doctor_list: List<Doctor>,
    val status_code: Int,
    val status_msg: String
) {
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
    )
}