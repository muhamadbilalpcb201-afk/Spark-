package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.SparkBluePrimary
import com.example.ui.theme.SparkCyanNeon

@Composable
fun YouTubeTopBar(
    unreadNotificationsCount: Int = 2,
    onCastClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Spark Custom Logo & Electric Branding
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { }
                .testTag("app_logo_row")
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color(0xFF0F172A)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_spark_logo),
                    contentDescription = "Spark Logo",
                    modifier = Modifier.size(34.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "SPARK",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.SansSerif
            )

            // Electric cyan dot
            Box(
                modifier = Modifier
                    .padding(start = 3.dp, bottom = 8.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(SparkCyanNeon)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Cast Icon
        IconButton(
            onClick = onCastClick,
            modifier = Modifier.testTag("topbar_cast_button")
        ) {
            Icon(
                imageVector = Icons.Default.Cast,
                contentDescription = "Cast to TV",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Notification Bell with Electric Blue badge
        Box(contentAlignment = Alignment.TopEnd) {
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier.testTag("topbar_notif_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            if (unreadNotificationsCount > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = (-4).dp, y = 6.dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(SparkBluePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$unreadNotificationsCount",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Search Icon
        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.testTag("topbar_search_button")
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // User Profile Avatar with Electric Blue Ring
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(34.dp)
                .clip(CircleShape)
                .background(SparkBluePrimary)
                .clickable { onProfileClick() }
                .testTag("topbar_profile_avatar"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "B",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}
