package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SparkBluePrimary
import com.example.ui.theme.SparkCyanNeon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBottomSheet(
    onDismiss: () -> Unit,
    onUploadVideo: (title: String, category: String, description: String) -> Unit,
    onImportYouTube: (urlOrId: String) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()
    var showUploadDialog by remember { mutableStateOf(false) }
    var showYouTubeImportDialog by remember { mutableStateOf(false) }

    var uploadTitle by remember { mutableStateOf("") }
    var uploadCategory by remember { mutableStateOf("Tech & Gadgets") }
    var uploadDesc by remember { mutableStateOf("") }
    var isPublishingNow by remember { mutableStateOf(false) }

    var youtubeUrlInput by remember { mutableStateOf("") }
    var isImportingNow by remember { mutableStateOf(false) }

    val categoryOptions = listOf("Tech & Gadgets", "Gaming", "Music", "Cooking & Food", "Karakoram Travel", "Trending")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("create_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Create on Spark",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            CreateActionItem(
                icon = Icons.Default.CloudDownload,
                title = "Import YouTube Video (URL or ID)",
                badge = "⚡ Online API",
                onClick = { showYouTubeImportDialog = true }
            )

            CreateActionItem(
                icon = Icons.Default.Upload,
                title = "Upload & Publish a Video",
                badge = "⚡ Instant Publish",
                onClick = { showUploadDialog = true }
            )

            CreateActionItem(
                icon = Icons.Default.FlashOn,
                title = "Create a Spark Short",
                badge = null,
                onClick = {
                    uploadCategory = "Shorts"
                    showUploadDialog = true
                }
            )

            CreateActionItem(
                icon = Icons.Default.LiveTv,
                title = "Go Live on Spark",
                badge = null,
                onClick = {
                    onUploadVideo("🔴 LIVE: Bilal's Studio Stream", "Live", "Streaming live to all Spark subscribers now!")
                    onDismiss()
                }
            )

            CreateActionItem(
                icon = Icons.Default.EditNote,
                title = "Create a Community Post",
                badge = null,
                onClick = { onDismiss() }
            )
        }
    }

    // Publish New Video Dialog
    if (showUploadDialog) {
        AlertDialog(
            onDismissRequest = { if (!isPublishingNow) showUploadDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Publish, contentDescription = null, tint = SparkCyanNeon)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publish to Spark", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = uploadTitle,
                        onValueChange = { uploadTitle = it },
                        label = { Text("Video Title") },
                        placeholder = { Text("e.g. Next-Gen Tech Review 2026") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_title_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Select Category:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 6.dp)
                    ) {
                        categoryOptions.forEach { cat ->
                            val isSel = cat == uploadCategory
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) SparkBluePrimary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { uploadCategory = cat }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    cat,
                                    color = if (isSel) Color.White else MaterialTheme.colorScheme.onBackground,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = uploadDesc,
                        onValueChange = { uploadDesc = it },
                        label = { Text("Description & Tags") },
                        placeholder = { Text("Details about your stream...") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedVisibility(visible = isPublishingNow) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = SparkBluePrimary, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Publishing and encoding stream on Spark...",
                                color = SparkCyanNeon,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (uploadTitle.isNotBlank()) {
                            isPublishingNow = true
                            onUploadVideo(
                                uploadTitle.trim(),
                                uploadCategory.trim(),
                                uploadDesc.trim()
                            )
                            showUploadDialog = false
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SparkBluePrimary),
                    enabled = uploadTitle.isNotBlank() && !isPublishingNow,
                    modifier = Modifier.testTag("confirm_publish_btn")
                ) {
                    Text("Publish Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    // YouTube Import Dialog
    if (showYouTubeImportDialog) {
        AlertDialog(
            onDismissRequest = { if (!isImportingNow) showYouTubeImportDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CloudDownload, contentDescription = null, tint = SparkCyanNeon)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Import from YouTube", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Paste any YouTube video link or ID. Spark will resolve and import the video right into your feed:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = youtubeUrlInput,
                        onValueChange = { youtubeUrlInput = it },
                        label = { Text("YouTube URL or Video ID") },
                        placeholder = { Text("https://www.youtube.com/watch?v=...") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("youtube_url_input")
                    )

                    AnimatedVisibility(visible = isImportingNow) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = SparkBluePrimary, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Fetching video stream metadata from YouTube...",
                                color = SparkCyanNeon,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (youtubeUrlInput.isNotBlank()) {
                            isImportingNow = true
                            onImportYouTube(youtubeUrlInput.trim())
                            showYouTubeImportDialog = false
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SparkBluePrimary),
                    enabled = youtubeUrlInput.isNotBlank() && !isImportingNow,
                    modifier = Modifier.testTag("confirm_import_btn")
                ) {
                    Text("Import & Play")
                }
            },
            dismissButton = {
                TextButton(onClick = { showYouTubeImportDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

@Composable
private fun CreateActionItem(
    icon: ImageVector,
    title: String,
    badge: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SparkCyanNeon,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            if (badge != null) {
                Text(
                    text = badge,
                    color = SparkBluePrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
