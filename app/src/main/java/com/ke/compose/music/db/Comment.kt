package com.ke.compose.music.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /**
     * 评论id
     */
    val commentId: Long,
    val username: String,
    @ColumnInfo(name = "user_avatar")
    val userAvatar: String,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    val content: String,
    @ColumnInfo("time_string")
    val timeString: String,
    val time: Long,
    @ColumnInfo("like_count")
    val likedCount: Int,

    @ColumnInfo("ip_location")
    val ipLocation: String,
    val owner: Boolean,
    val liked: Boolean,


    /**
     * 子评论数
     */
    val replyCount: Int = 0,

    )
