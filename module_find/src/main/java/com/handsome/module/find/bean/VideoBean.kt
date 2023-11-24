package com.handsome.module.find.bean

data class VideoBean(
    val next_time: Int,
    val status_code: Int,
    val status_msg: String,
    val video_list: List<Video>
) {
    data class Video(
        val author: Author,
        val comment_count: Int,
        val cover_url: String,
        val favorite_count: Int,
        val id: Long,
        val is_favorite: Boolean,
        val play_url: String,
        val title: String
    ) {
        data class Author(
            val avatar: String,
            val background_image: String,
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
}