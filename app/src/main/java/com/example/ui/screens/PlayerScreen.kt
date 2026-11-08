package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownloadDone
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Video
import com.example.ui.components.VideoCard
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.SparkBluePrimary
import com.example.ui.theme.SparkCyanNeon

@Composable
fun PlayerScreen(
    video: Video,
    relatedVideos: List<Video>,
    isPlaying: Boolean,
    progress: Float,
    speed: Float,
    quality: String,
    isFullscreen: Boolean,
    isAutoplay: Boolean,
    onPlayPauseToggle: () -> Unit,
    onSeek: (Float) -> Unit,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onNext: () -> Unit,
    onToggleFullscreen: () -> Unit,
    onToggleAutoplay: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    onQualityChange: (String) -> Unit,
    onClosePlayer: () -> Unit,
    onLikeToggle: () -> Unit,
    onDislikeToggle: () -> Unit,
    onSubscribeToggle: () -> Unit,
    onDownloadToggle: () -> Unit,
    onWatchLaterToggle: () -> Unit,
    onCommentsOpen: () -> Unit,
    onSelectRelatedVideo: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    var showAiSummaryModal by remember { mutableStateOf(false) }
    val actionsScrollState = rememberScrollState()

    BackHandler {
        if (isFullscreen) {
            onToggleFullscreen()
        } else {
            onClosePlayer()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("player_screen")
    ) {
        VideoPlayerView(
            video = video,
            isPlaying = isPlaying,
            progress = progress,
            speed = speed,
            quality = quality,
            isFullscreen = isFullscreen,
            isAutoplay = isAutoplay,
            onPlayPauseToggle = onPlayPauseToggle,
            onSeek = onSeek,
            onRewind = onRewind,
            onForward = onForward,
            onNext = onNext,
            onToggleFullscreen = onToggleFullscreen,
            onToggleAutoplay = onToggleAutoplay,
            onSpeedChange = onSpeedChange,
            onQualityChange = onQualityChange,
            onClosePlayer = onClosePlayer,
            onOpenAiSummary = { showAiSummaryModal = true }
        )

        if (!isFullscreen) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    // Video Title
                    Text(
                        text = video.title,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    )

                    // Views & Description Card
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${video.views}   ${video.uploadDate}",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = if (isDescriptionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = video.description,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            maxLines = if (isDescriptionExpanded) 20 else 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Channel Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(video.channel.avatarColorHex)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = video.channel.name.take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = video.channel.name,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${video.channel.subscribers} subscribers",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }

                        // Subscribe button
                        Button(
                            onClick = onSubscribeToggle,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (video.channel.isSubscribed)
                                    MaterialTheme.colorScheme.surfaceVariant
                                else SparkBluePrimary
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.height(38.dp).testTag("subscribe_btn")
                        ) {
                            if (video.channel.isSubscribed) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = SparkCyanNeon,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Subscribed",
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Text(
                                    text = "Subscribe",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Horizontal Actions Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(actionsScrollState)
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Like & Dislike combined pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { onLikeToggle() }
                                    .testTag("action_like_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ThumbUp,
                                    contentDescription = "Like",
                                    tint = if (video.isLiked) SparkCyanNeon else MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${video.likesCount / 1000}K",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))
                            Divider(
                                modifier = Modifier
                                    .height(16.dp)
                                    .width(1.dp),
                                color = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(12.dp))

                            Icon(
                                imageVector = Icons.Default.ThumbDown,
                                contentDescription = "Dislike",
                                tint = if (video.isDisliked) SparkCyanNeon else MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onDislikeToggle() }
                            )
                        }

                        // Spark AI Summary Button
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF003875))
                                .clickable { showAiSummaryModal = true }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Summary",
                                tint = SparkCyanNeon,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Spark AI",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Share Action
                        PlayerActionPill(
                            icon = Icons.Default.Share,
                            label = "Share",
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Watch '${video.title}' on Spark: https://spark.app/v/${video.id}")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Video"))
                            }
                        )

                        // Download Action
                        PlayerActionPill(
                            icon = if (video.isDownloaded) Icons.Default.FileDownloadDone else Icons.Default.Download,
                            label = if (video.isDownloaded) "Downloaded" else "Download",
                            onClick = onDownloadToggle
                        )

                        // Save to Watch Later
                        PlayerActionPill(
                            icon = Icons.Default.BookmarkBorder,
                            label = if (video.isSavedToWatchLater) "Saved" else "Save",
                            onClick = onWatchLaterToggle
                        )

                        // Clip Action
                        PlayerActionPill(
                            icon = Icons.Default.ContentCut,
                            label = "Clip",
                            onClick = { }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Comments Preview Card
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onCommentsOpen() }
                            .padding(12.dp)
                            .testTag("player_comments_card")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Comments",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${video.commentsCount}",
                                color = SparkCyanNeon,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (video.topComment != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(video.topComment.authorColorHex)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = video.topComment.authorName.take(1),
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = video.topComment.text,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Related / Recommended Videos List
                items(relatedVideos.filterNot { it.id == video.id }, key = { it.id }) { related ->
                    VideoCard(
                        video = related,
                        onClick = { onSelectRelatedVideo(related) }
                    )
                }
            }
        }
    }

    // Spark AI Summary Modal
    if (showAiSummaryModal) {
        AlertDialog(
            onDismissRequest = { showAiSummaryModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = SparkCyanNeon,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Spark AI Video Key Takeaways", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "⚡ Key Highlights & Timestamps:",
                        color = SparkCyanNeon,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• 00:00 - Introduction and context overview\n• 03:45 - Key architectural and technical innovations\n• 08:20 - Real world live performance & benchmark test\n• 12:10 - Final conclusion & expert recommendation",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAiSummaryModal = false }) {
                    Text("Close", color = SparkBluePrimary)
                }
            }
        )
    }
}

@Composable
private fun PlayerActionPill(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
