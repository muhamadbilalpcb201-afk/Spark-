package com.example.data.api

import com.example.data.model.Channel
import com.example.data.model.Comment
import com.example.data.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object YouTubeOnlineService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    /**
     * Resolves real YouTube video metadata using YouTube's official oEmbed API.
     * Takes any YouTube video ID or full URL and extracts real title, channel author, and thumbnail.
     */
    suspend fun fetchYouTubeVideoByUrl(youtubeUrlOrId: String): Video? = withContext(Dispatchers.IO) {
        try {
            val videoUrl = if (youtubeUrlOrId.startsWith("http")) {
                youtubeUrlOrId
            } else {
                "https://www.youtube.com/watch?v=$youtubeUrlOrId"
            }

            val requestUrl = "https://www.youtube.com/oembed?url=${java.net.URLEncoder.encode(videoUrl, "UTF-8")}&format=json"
            val request = Request.Builder().url(requestUrl).build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null

            val body = response.body?.string() ?: return@withContext null
            val json = JSONObject(body)

            val title = json.optString("title", "Imported YouTube Video")
            val authorName = json.optString("author_name", "YouTube Creator")
            val thumbnailUrl = json.optString("thumbnail_url", "")

            val channel = Channel(
                id = "ch_yt_${System.currentTimeMillis()}",
                name = authorName,
                handle = "@${authorName.lowercase().replace(" ", "_")}",
                subscribers = "1.5M",
                avatarColorHex = 0xFF0077FF,
                isVerified = true,
                isSubscribed = false
            )

            Video(
                id = "yt_${System.currentTimeMillis()}",
                title = title,
                description = "Imported from YouTube: $videoUrl\nEnjoy high quality playback on Spark.\n\n#spark #youtube #streaming",
                channel = channel,
                views = "450K views",
                uploadDate = "Trending on YouTube",
                duration = "12:30",
                durationSeconds = 750,
                thumbnailUrl = thumbnailUrl,
                category = "Trending",
                likesCount = 38000,
                commentsCount = 890,
                topComment = Comment(
                    id = "c_imported",
                    authorName = "Spark Streamer",
                    authorHandle = "@spark_fan",
                    authorColorHex = 0xFF00E5FF,
                    timestamp = "1 hour ago",
                    text = "Streaming smoothly on Spark with Electric Blue theme!",
                    likesCount = 142
                )
            )
        } catch (e: Exception) {
            null
        }
    }
}
