package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShortItem
import com.example.data.model.Video
import com.example.ui.components.CategoryChips
import com.example.ui.components.ShortsShelf
import com.example.ui.components.VideoCard

@Composable
fun HomeScreen(
    videos: List<Video>,
    shorts: List<ShortItem>,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onVideoClick: (Video) -> Unit,
    onShortClick: (ShortItem) -> Unit,
    onWatchLaterClick: (Video) -> Unit,
    onDownloadClick: (Video) -> Unit,
    onShareClick: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen")
    ) {
        // Horizontal Category Chips
        CategoryChips(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected
        )

        if (videos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No videos found in \"$selectedCategory\"",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("home_feed_list")
            ) {
                itemsIndexed(videos, key = { _, video -> video.id }) { index, video ->
                    VideoCard(
                        video = video,
                        onClick = { onVideoClick(video) },
                        onWatchLaterClick = { onWatchLaterClick(video) },
                        onDownloadClick = { onDownloadClick(video) },
                        onShareClick = { onShareClick(video) }
                    )

                    // Insert Shorts Shelf after 2nd video (index 1)
                    if (index == 1 && shorts.isNotEmpty()) {
                        ShortsShelf(
                            shorts = shorts,
                            onShortClick = onShortClick
                        )
                    }
                }
            }
        }
    }
}
