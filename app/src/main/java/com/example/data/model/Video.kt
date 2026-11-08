package com.example.data.model

data class Video(
    val id: String,
    val title: String,
    val description: String,
    val channel: Channel,
    val views: String,
    val uploadDate: String,
    val duration: String,
    val durationSeconds: Int,
    val thumbnailResId: Int? = null,
    val thumbnailUrl: String = "",
    val category: String,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val isSavedToWatchLater: Boolean = false,
    val isDownloaded: Boolean = false,
    val commentsCount: Int,
    val topComment: Comment? = null
)

data class Channel(
    val id: String,
    val name: String,
    val handle: String,
    val subscribers: String,
    val avatarUrl: String = "",
    val avatarColorHex: Long = 0xFFE53935,
    val isVerified: Boolean = true,
    val isSubscribed: Boolean = false,
    val hasUnreadStories: Boolean = true
)

data class ShortItem(
    val id: String,
    val title: String,
    val channel: Channel,
    val views: String,
    val likesCount: String,
    val commentsCount: String,
    val audioTrackName: String,
    val imageResId: Int? = null,
    val imageUrl: String = "",
    val isLiked: Boolean = false
)

data class Comment(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val authorColorHex: Long = 0xFF1E88E5,
    val timestamp: String,
    val text: String,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val isHeartedByCreator: Boolean = false
)

data class Playlist(
    val id: String,
    val title: String,
    val videoCount: Int,
    val thumbnailResId: Int? = null,
    val isPrivate: Boolean = true
)

data class NotificationItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val timestamp: String,
    val avatarColorHex: Long,
    val isRead: Boolean = false
)
