package com.ke.compose.music.ui.comments

/**
 * 评论类型
 */
enum class CommentType(val type: Int) {
    /**
     * 音乐
     */
    Music(0),

    /**
     * 歌单
     */
    Playlist(2),

    /**
     * 专辑
     */
    Album(3)
}