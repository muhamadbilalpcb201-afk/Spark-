package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Playlist
import com.example.data.model.Video
import com.example.ui.theme.SparkBluePrimary
import com.example.ui.theme.SparkCyanNeon

@Composable
fun YouScreen(
    currentAccount: String = "Bilal Muhammad (Main)",
    watchHistory: List<Video>,
    userUploadedVideos: List<Video> = emptyList(),
    downloadedVideos: List<Video> = emptyList(),
    playlists: List<Playlist>,
    isDarkTheme: Boolean,
    onToggleDarkTheme: () -> Unit,
    onVideoClick: (Video) -> Unit,
    onClearHistory: () -> Unit,
    onCreatePlaylist: (String) -> Unit,
    onSwitchAccount: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val historyScrollState = rememberScrollState()
    val playlistScrollState = rememberScrollState()

    var showNewPlaylistDialog by remember { mutableStateOf(false) }
    var newPlaylistName by remember { mutableStateOf("") }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showSwitchAccountDialog by remember { mutableStateOf(false) }
    var showDownloadsDialog by remember { mutableStateOf(false) }
    var showUploadsDialog by remember { mutableStateOf(false) }

    val accountsList = listOf(
        "Bilal Muhammad (Main)",
        "Bilal Studio Production",
        "Spark Pro Guest"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(bottom = 28.dp)
            .testTag("you_screen")
    ) {
        // User Profile Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(SparkBluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentAccount.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = currentAccount,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "@bilal_spark • Spark Creator",
                    color = SparkCyanNeon,
                    fontSize = 13.sp
                )
            }

            IconButton(onClick = { showSettingsDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Account Action Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AccountActionPill(
                icon = Icons.Default.SwitchAccount,
                label = "Switch account",
                onClick = { showSwitchAccountDialog = true }
            )
            AccountActionPill(
                icon = Icons.Default.Security,
                label = "Google Account",
                onClick = { }
            )
            AccountActionPill(
                icon = Icons.Default.History,
                label = "Incognito",
                onClick = { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // History Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "History",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Clear all",
                color = SparkCyanNeon,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { onClearHistory() }
                    .padding(4.dp)
            )
        }

        if (watchHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "No watched videos yet. Watch videos to see your history here.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(historyScrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                watchHistory.forEach { video ->
                    Column(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .width(160.dp)
                            .clickable { onVideoClick(video) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF131D31))
                        ) {
                            if (video.thumbnailResId != null) {
                                Image(
                                    painter = painterResource(id = video.thumbnailResId),
                                    contentDescription = video.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xEE090D16))
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = video.duration,
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            }

                            LinearProgressIndicator(
                                progress = { 0.45f },
                                color = SparkCyanNeon,
                                trackColor = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .align(Alignment.BottomCenter)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = video.title,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = video.channel.name,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(12.dp))

        // Playlists Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Playlists",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { showNewPlaylistDialog = true }
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = SparkCyanNeon,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "New playlist",
                    color = SparkCyanNeon,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(playlistScrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            playlists.forEach { playlist ->
                Column(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .width(140.dp)
                        .clickable { }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF131D31))
                    ) {
                        if (playlist.thumbnailResId != null) {
                            Image(
                                painter = painterResource(id = playlist.thumbnailResId),
                                contentDescription = playlist.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxHeight()
                                .width(40.dp)
                                .background(Color(0xBB090D16)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.PlaylistPlay,
                                    contentDescription = null,
                                    tint = SparkCyanNeon,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "${playlist.videoCount}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = playlist.title,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = if (playlist.isPrivate) "Private" else "Public",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(8.dp))

        // Library Menu Items with Live Counters
        LibraryMenuItem(
            icon = Icons.Default.VideoLibrary,
            title = "Your published videos",
            subtitle = "${userUploadedVideos.size} videos live",
            onClick = { showUploadsDialog = true }
        )
        LibraryMenuItem(
            icon = Icons.Default.Download,
            title = "Downloads",
            subtitle = "${downloadedVideos.size} videos available offline",
            onClick = { showDownloadsDialog = true }
        )
        LibraryMenuItem(
            icon = Icons.Default.Movie,
            title = "Your movies & TV",
            subtitle = null,
            onClick = { }
        )
        LibraryMenuItem(
            icon = Icons.Default.HelpOutline,
            title = "Help and feedback",
            subtitle = "Download & APK instructions",
            onClick = { showSettingsDialog = true }
        )
    }

    // Switch Account Dialog
    if (showSwitchAccountDialog) {
        AlertDialog(
            onDismissRequest = { showSwitchAccountDialog = false },
            title = { Text("Switch Spark Account", color = MaterialTheme.colorScheme.onSurface) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    accountsList.forEach { acc ->
                        val isCurrent = acc == currentAccount
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSwitchAccount(acc)
                                    showSwitchAccountDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (isCurrent) SparkBluePrimary else MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(acc.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(acc, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                            if (isCurrent) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = SparkCyanNeon)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSwitchAccountDialog = false }) {
                    Text("Close", color = SparkBluePrimary)
                }
            }
        )
    }

    // Published Uploads Dialog
    if (showUploadsDialog) {
        AlertDialog(
            onDismissRequest = { showUploadsDialog = false },
            title = { Text("Your Published Videos", color = MaterialTheme.colorScheme.onSurface) },
            text = {
                if (userUploadedVideos.isEmpty()) {
                    Text("You have not published any videos yet. Tap the (+) Create button on bottom bar to publish your first video!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        userUploadedVideos.forEach { vid ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onVideoClick(vid)
                                        showUploadsDialog = false
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.VideoLibrary, contentDescription = null, tint = SparkCyanNeon)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(vid.title, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp, maxLines = 1)
                                    Text("${vid.views} • ${vid.uploadDate}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showUploadsDialog = false }) {
                    Text("Close", color = SparkBluePrimary)
                }
            }
        )
    }

    // Offline Downloads Dialog
    if (showDownloadsDialog) {
        AlertDialog(
            onDismissRequest = { showDownloadsDialog = false },
            title = { Text("Offline Downloads", color = MaterialTheme.colorScheme.onSurface) },
            text = {
                if (downloadedVideos.isEmpty()) {
                    Text("No videos downloaded yet. Tap the Download button below any video to watch it offline anytime!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        downloadedVideos.forEach { vid ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onVideoClick(vid)
                                        showDownloadsDialog = false
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = SparkBluePrimary)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(vid.title, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp, maxLines = 1)
                                    Text("Ready for offline streaming • ${vid.duration}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDownloadsDialog = false }) {
                    Text("Close", color = SparkBluePrimary)
                }
            }
        )
    }

    // New Playlist Dialog
    if (showNewPlaylistDialog) {
        AlertDialog(
            onDismissRequest = { showNewPlaylistDialog = false },
            title = { Text("New playlist", color = MaterialTheme.colorScheme.onSurface) },
            text = {
                OutlinedTextField(
                    value = newPlaylistName,
                    onValueChange = { newPlaylistName = it },
                    placeholder = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPlaylistName.isNotBlank()) {
                            onCreatePlaylist(newPlaylistName.trim())
                            newPlaylistName = ""
                            showNewPlaylistDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SparkBluePrimary)
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewPlaylistDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    // Settings Dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Spark Settings", color = MaterialTheme.colorScheme.onSurface) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Electric Obsidian Dark theme",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { onToggleDarkTheme() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SparkBluePrimary
                            )
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "📱 How to install on your mobile device:\n1. Look at the top right settings / project menu in AI Studio.\n2. Tap 'Export APK' or 'Download Project ZIP'.\n3. You can also share the web emulator link directly on your mobile browser!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Spark v2.0 • Electric Blue Streaming Edition",
                        fontSize = 11.sp,
                        color = SparkCyanNeon
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("Done", color = SparkBluePrimary)
                }
            }
        )
    }
}

@Composable
private fun AccountActionPill(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SparkCyanNeon,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LibraryMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SparkBluePrimary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(18.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }

        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
        )
    }
}
