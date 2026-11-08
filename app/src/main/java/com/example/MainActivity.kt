package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.YouTubeTopBar
import com.example.ui.screens.CastDialog
import com.example.ui.screens.CreateBottomSheet
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsBottomSheet
import com.example.ui.screens.PlayerScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.ShortsScreen
import com.example.ui.screens.SubscriptionsScreen
import com.example.ui.screens.YouScreen
import com.example.ui.theme.SparkBluePrimary
import com.example.ui.theme.SparkCyanNeon
import com.example.ui.theme.SparkTheme
import com.example.ui.viewmodel.YouTubeNavTab
import com.example.ui.viewmodel.YouTubeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: YouTubeViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

            SparkTheme(darkTheme = isDarkTheme) {
                MainSparkApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainSparkApp(viewModel: YouTubeViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val activeVideo by viewModel.activeVideo.collectAsStateWithLifecycle()
    val filteredVideos by viewModel.filteredVideos.collectAsStateWithLifecycle()
    val allVideos by viewModel.videos.collectAsStateWithLifecycle()
    val userUploadedVideos by viewModel.userUploadedVideos.collectAsStateWithLifecycle()
    val downloadedVideos by viewModel.downloadedVideos.collectAsStateWithLifecycle()
    val shorts by viewModel.shorts.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val progress by viewModel.playbackProgress.collectAsStateWithLifecycle()
    val speed by viewModel.playbackSpeed.collectAsStateWithLifecycle()
    val quality by viewModel.playbackQuality.collectAsStateWithLifecycle()
    val isFullscreen by viewModel.isFullscreen.collectAsStateWithLifecycle()
    val isAutoplay by viewModel.isAutoplay.collectAsStateWithLifecycle()
    val isCommentsSheetOpen by viewModel.isCommentsSheetOpen.collectAsStateWithLifecycle()
    val isCreateSheetOpen by viewModel.isCreateSheetOpen.collectAsStateWithLifecycle()
    val isNotificationsOpen by viewModel.isNotificationsOpen.collectAsStateWithLifecycle()
    val isCastDialogOpen by viewModel.isCastDialogOpen.collectAsStateWithLifecycle()
    val isSearchOpen by viewModel.isSearchOpen.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val watchHistory by viewModel.watchHistory.collectAsStateWithLifecycle()
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val currentAccount by viewModel.currentAccount.collectAsStateWithLifecycle()
    val publishStatusMessage by viewModel.publishStatusMessage.collectAsStateWithLifecycle()
    val subscribedChannels = viewModel.subscribedChannels
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(publishStatusMessage) {
        publishStatusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                if (currentTab != YouTubeNavTab.SHORTS) {
                    YouTubeTopBar(
                        unreadNotificationsCount = notifications.count { !it.isRead },
                        onCastClick = { viewModel.openCastDialog() },
                        onNotificationsClick = { viewModel.openNotifications() },
                        onSearchClick = { viewModel.openSearch() },
                        onProfileClick = { viewModel.selectTab(YouTubeNavTab.YOU) },
                        modifier = Modifier.statusBarsPadding()
                    )
                }
            },
            bottomBar = {
                SparkBottomNavigation(
                    currentTab = currentTab,
                    accountName = currentAccount,
                    onTabSelected = { tab ->
                        if (tab == null) {
                            viewModel.openCreate()
                        } else {
                            viewModel.selectTab(tab)
                        }
                    },
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentTab) {
                    YouTubeNavTab.HOME -> {
                        HomeScreen(
                            videos = filteredVideos,
                            shorts = shorts,
                            categories = viewModel.categories,
                            selectedCategory = selectedCategory,
                            onCategorySelected = { viewModel.selectCategory(it) },
                            onVideoClick = { viewModel.playVideo(it) },
                            onShortClick = {
                                viewModel.selectTab(YouTubeNavTab.SHORTS)
                            },
                            onWatchLaterClick = { viewModel.toggleWatchLater(it) },
                            onDownloadClick = { viewModel.toggleDownload(it) },
                            onShareClick = { }
                        )
                    }

                    YouTubeNavTab.SHORTS -> {
                        ShortsScreen(
                            shorts = shorts,
                            onLikeToggle = { viewModel.toggleShortLike(it) },
                            onCommentsOpen = { viewModel.openComments() },
                            onShareClick = { },
                            onSearchClick = { viewModel.openSearch() }
                        )
                    }

                    YouTubeNavTab.SUBSCRIPTIONS -> {
                        SubscriptionsScreen(
                            subscribedChannels = subscribedChannels,
                            videos = allVideos,
                            onVideoClick = { viewModel.playVideo(it) },
                            onWatchLaterClick = { viewModel.toggleWatchLater(it) },
                            onDownloadClick = { viewModel.toggleDownload(it) },
                            onShareClick = { }
                        )
                    }

                    YouTubeNavTab.YOU -> {
                        YouScreen(
                            currentAccount = currentAccount,
                            watchHistory = watchHistory,
                            userUploadedVideos = userUploadedVideos,
                            downloadedVideos = downloadedVideos,
                            playlists = playlists,
                            isDarkTheme = isDarkTheme,
                            onToggleDarkTheme = { viewModel.toggleDarkTheme() },
                            onVideoClick = { viewModel.playVideo(it) },
                            onClearHistory = { viewModel.clearHistory() },
                            onCreatePlaylist = { viewModel.createPlaylist(it) },
                            onSwitchAccount = { viewModel.switchAccount(it) }
                        )
                    }
                }
            }
        }

        // Full Screen Search Overlay
        if (isSearchOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                SearchScreen(
                    query = searchQuery,
                    searchResults = filteredVideos,
                    onQueryChange = { viewModel.updateSearchQuery(it) },
                    onCloseSearch = { viewModel.closeSearch() },
                    onVideoClick = { viewModel.playVideo(it) }
                )
            }
        }

        // Active Video Player Overlay
        AnimatedVisibility(
            visible = activeVideo != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            activeVideo?.let { currentVideo ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    PlayerScreen(
                        video = currentVideo,
                        relatedVideos = allVideos,
                        isPlaying = isPlaying,
                        progress = progress,
                        speed = speed,
                        quality = quality,
                        isFullscreen = isFullscreen,
                        isAutoplay = isAutoplay,
                        onPlayPauseToggle = { viewModel.togglePlayPause() },
                        onSeek = { viewModel.seekTo(it) },
                        onRewind = { viewModel.rewind10() },
                        onForward = { viewModel.forward10() },
                        onNext = { viewModel.playNextVideo() },
                        onToggleFullscreen = { viewModel.toggleFullscreen() },
                        onToggleAutoplay = { viewModel.toggleAutoplay() },
                        onSpeedChange = { viewModel.setPlaybackSpeed(it) },
                        onQualityChange = { viewModel.setPlaybackQuality(it) },
                        onClosePlayer = { viewModel.closePlayer() },
                        onLikeToggle = { viewModel.toggleLike(currentVideo) },
                        onDislikeToggle = { viewModel.toggleDislike(currentVideo) },
                        onSubscribeToggle = { viewModel.toggleSubscribe(currentVideo.channel.id) },
                        onDownloadToggle = { viewModel.toggleDownload(currentVideo) },
                        onWatchLaterToggle = { viewModel.toggleWatchLater(currentVideo) },
                        onCommentsOpen = { viewModel.openComments() },
                        onSelectRelatedVideo = { viewModel.playVideo(it) }
                    )
                }
            }
        }

        // Comments Bottom Sheet
        if (isCommentsSheetOpen && activeVideo != null) {
            CommentsBottomSheet(
                video = activeVideo!!,
                onDismiss = { viewModel.closeComments() },
                onAddComment = { text -> viewModel.addComment(activeVideo!!.id, text) }
            )
        }

        // Create & Publish (+) Bottom Sheet
        if (isCreateSheetOpen) {
            CreateBottomSheet(
                onDismiss = { viewModel.closeCreate() },
                onUploadVideo = { title, cat, desc ->
                    viewModel.publishVideo(title, cat, desc) {
                        viewModel.closeCreate()
                    }
                },
                onImportYouTube = { url ->
                    viewModel.importYouTubeVideo(url) {
                        viewModel.closeCreate()
                    }
                }
            )
        }

        // Notifications Bottom Sheet
        if (isNotificationsOpen) {
            NotificationsBottomSheet(
                notifications = notifications,
                onDismiss = { viewModel.closeNotifications() }
            )
        }

        // Cast to TV Dialog
        if (isCastDialogOpen) {
            CastDialog(onDismiss = { viewModel.closeCastDialog() })
        }
    }
}

@Composable
fun SparkBottomNavigation(
    currentTab: YouTubeNavTab,
    accountName: String,
    onTabSelected: (YouTubeNavTab?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavItem(
                selected = currentTab == YouTubeNavTab.HOME,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                label = "Home",
                onClick = { onTabSelected(YouTubeNavTab.HOME) },
                testTag = "nav_tab_home"
            )

            BottomNavItem(
                selected = currentTab == YouTubeNavTab.SHORTS,
                selectedIcon = Icons.Filled.FlashOn,
                unselectedIcon = Icons.Outlined.FlashOn,
                label = "Shorts",
                onClick = { onTabSelected(YouTubeNavTab.SHORTS) },
                testTag = "nav_tab_shorts"
            )

            // Center Glowing (+) Spark Action Button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SparkBluePrimary)
                    .clickable { onTabSelected(null) }
                    .testTag("nav_tab_create"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            BottomNavItem(
                selected = currentTab == YouTubeNavTab.SUBSCRIPTIONS,
                selectedIcon = Icons.Filled.Subscriptions,
                unselectedIcon = Icons.Outlined.Subscriptions,
                label = "Subscriptions",
                onClick = { onTabSelected(YouTubeNavTab.SUBSCRIPTIONS) },
                testTag = "nav_tab_subscriptions"
            )

            // You (Profile Avatar tab)
            Column(
                modifier = Modifier
                    .clickable { onTabSelected(YouTubeNavTab.YOU) }
                    .padding(horizontal = 8.dp)
                    .testTag("nav_tab_you"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(SparkBluePrimary)
                        .then(
                            if (currentTab == YouTubeNavTab.YOU) {
                                Modifier.border(2.dp, SparkCyanNeon, CircleShape)
                            } else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = accountName.take(1).uppercase(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "You",
                    color = if (currentTab == YouTubeNavTab.YOU)
                        SparkCyanNeon
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = if (currentTab == YouTubeNavTab.YOU) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    selected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (selected) selectedIcon else unselectedIcon,
            contentDescription = label,
            tint = if (selected) SparkCyanNeon else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            color = if (selected) SparkCyanNeon else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
