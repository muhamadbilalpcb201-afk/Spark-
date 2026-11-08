package com.example.data.repository

import com.example.R
import com.example.data.api.YouTubeOnlineService
import com.example.data.model.Channel
import com.example.data.model.Comment
import com.example.data.model.NotificationItem
import com.example.data.model.Playlist
import com.example.data.model.ShortItem
import com.example.data.model.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VideoRepository {

    private val techChannel = Channel(
        id = "ch_tech",
        name = "TechSpark Pro",
        handle = "@techspark_official",
        subscribers = "4.8M",
        avatarColorHex = 0xFF0077FF,
        isVerified = true,
        isSubscribed = true
    )

    private val foodChannel = Channel(
        id = "ch_food",
        name = "Royal Gourmet Flavors",
        handle = "@royal_flavors",
        subscribers = "2.1M",
        avatarColorHex = 0xFF00B4D8,
        isVerified = true,
        isSubscribed = false
    )

    private val travelChannel = Channel(
        id = "ch_travel",
        name = "Karakoram Explorers",
        handle = "@karakoram_wild",
        subscribers = "1.2M",
        avatarColorHex = 0xFF0096C7,
        isVerified = true,
        isSubscribed = true
    )

    private val gamingChannel = Channel(
        id = "ch_gaming",
        name = "CyberNova Gaming",
        handle = "@cybernova_live",
        subscribers = "3.6M",
        avatarColorHex = 0xFF023E8A,
        isVerified = true,
        isSubscribed = false
    )

    private val musicChannel = Channel(
        id = "ch_music",
        name = "Cyber Lofi Chillout",
        handle = "@lofi_cyber_beats",
        subscribers = "7.2M",
        avatarColorHex = 0xFF0077B6,
        isVerified = true,
        isSubscribed = true
    )

    private val podcastsChannel = Channel(
        id = "ch_podcast",
        name = "The Spark Dialogue",
        handle = "@spark_dialogue",
        subscribers = "1.4M",
        avatarColorHex = 0xFF48CAE4,
        isVerified = true,
        isSubscribed = false
    )

    private val initialVideos = listOf(
        Video(
            id = "vid_tech_1",
            title = "Next-Gen Quantum Smartphone Teardown: Neural Chip & 200W Wireless Charge",
            description = "An exclusive deep dive into the 2026 flagship device with cryogenic liquid cooling, titanium sub-chassis, and real-time AI optical sensor.\n\n#tech #smartphone #quantum #hardware #spark",
            channel = techChannel,
            views = "1.8M views",
            uploadDate = "2 days ago",
            duration = "14:28",
            durationSeconds = 868,
            thumbnailResId = R.drawable.thumb_tech_review,
            category = "Tech & Gadgets",
            likesCount = 124000,
            commentsCount = 3100,
            topComment = Comment(
                id = "c_1",
                authorName = "Hamza Ali",
                authorHandle = "@hamza_spark",
                authorColorHex = 0xFF0077FF,
                timestamp = "1 day ago",
                text = "The thermal benchmarking at 09:15 is insane! Best video on the platform.",
                likesCount = 1420,
                isHeartedByCreator = true
            )
        ),
        Video(
            id = "vid_food_1",
            title = "Grand Master Royal Biryani: Centuries-Old Dum Technique in Cast Iron",
            description = "Learn the authentic secret Dum Biryani recipe passed down from grand royal kitchens. Saffron-infused long-grain basmati and secret caramelized spices.\n\n#food #biryani #cooking #culinary #chef",
            channel = foodChannel,
            views = "3.8M views",
            uploadDate = "4 days ago",
            duration = "18:45",
            durationSeconds = 1125,
            thumbnailResId = R.drawable.thumb_biryani_recipe,
            category = "Cooking & Food",
            likesCount = 310000,
            commentsCount = 4900,
            topComment = Comment(
                id = "c_2",
                authorName = "Ayesha Khan",
                authorHandle = "@ayesha_culinary",
                authorColorHex = 0xFF00E5FF,
                timestamp = "3 days ago",
                text = "Cooked this for our family feast! The aroma filled the entire street. Brilliant!",
                likesCount = 980,
                isHeartedByCreator = true
            )
        ),
        Video(
            id = "vid_travel_1",
            title = "Karakoram & Hunza 8K Expedition: Traversing Frozen Glaciers & Ancient Peaks",
            description = "A cinematic masterclass journey through Passu Cones, Attabad Lake, and treacherous high-altitude mountain passes at 5,000 meters.\n\n#travel #karakoram #pakistan #mountains #cinematic",
            channel = travelChannel,
            views = "1.1M views",
            uploadDate = "1 week ago",
            duration = "22:10",
            durationSeconds = 1330,
            thumbnailResId = R.drawable.thumb_travel_mountains,
            category = "Karakoram Travel",
            likesCount = 96000,
            commentsCount = 1840,
            topComment = Comment(
                id = "c_3",
                authorName = "David Chen",
                authorHandle = "@david_explore",
                authorColorHex = 0xFF38BDF8,
                timestamp = "5 days ago",
                text = "The drone shot over the mountain crest at sunset made me gasp. Absolutely majestic.",
                likesCount = 670,
                isHeartedByCreator = false
            )
        ),
        Video(
            id = "vid_gaming_1",
            title = "Cyber Championship Grand Finals: The Unstoppable 1v5 Tactical Comeback!",
            description = "Witness the greatest esports round in history! Tactical smoke positioning, precise clutch headshots, and an impossible defuse in overtime.\n\n#gaming #esports #clutch #cybernova #live",
            channel = gamingChannel,
            views = "2.7M views",
            uploadDate = "3 days ago",
            duration = "11:52",
            durationSeconds = 712,
            thumbnailResId = R.drawable.thumb_gaming_epic,
            category = "Gaming",
            likesCount = 210000,
            commentsCount = 4200,
            topComment = Comment(
                id = "c_4",
                authorName = "Zarrar Gamer",
                authorHandle = "@zarrar_pro",
                authorColorHex = 0xFF0077FF,
                timestamp = "2 days ago",
                text = "I still cannot believe that flick shot through the smoke! Insane skill.",
                likesCount = 890
            )
        ),
        Video(
            id = "vid_music_1",
            title = "Midnight Neon Lofi Beats: Electric Synth Chill for Coding & Focus [24/7 Stream]",
            description = "Immerse yourself into deep flow state with calming cyberpunk ambient melodies, gentle rain, and smooth jazz chords.\n\n#lofi #codingbeats #focus #midnight #ambient",
            channel = musicChannel,
            views = "18.4M views",
            uploadDate = "Streamed Live",
            duration = "1:45:00",
            durationSeconds = 6300,
            thumbnailResId = R.drawable.thumb_tech_review,
            category = "Music",
            likesCount = 890000,
            commentsCount = 11200,
            topComment = Comment(
                id = "c_5",
                authorName = "Zainab Noor",
                authorHandle = "@zainab_code",
                authorColorHex = 0xFF00E5FF,
                timestamp = "2 hours ago",
                text = "Helped me debug a complex backend architecture without burning out. Essential companion!",
                likesCount = 420
            )
        ),
        Video(
            id = "vid_pod_1",
            title = "Autonomous Software & The Future of Human Intellect: Dr. Farooq Keynote",
            description = "What happens when software designs software? An eye-opening 60-minute discussion on agentic systems and creative breakthroughs.\n\n#podcast #technology #future #ai #innovation",
            channel = podcastsChannel,
            views = "820K views",
            uploadDate = "5 days ago",
            duration = "48:15",
            durationSeconds = 2895,
            thumbnailResId = R.drawable.thumb_gaming_epic,
            category = "Podcasts",
            likesCount = 64000,
            commentsCount = 1100,
            topComment = Comment(
                id = "c_6",
                authorName = "Usman Farooq",
                authorHandle = "@usman_tech",
                authorColorHex = 0xFF0077FF,
                timestamp = "4 days ago",
                text = "The vision shared about collaborative agents is groundbreaking.",
                likesCount = 310
            )
        )
    )

    private val initialShorts = listOf(
        ShortItem(
            id = "short_1",
            title = "The secret glowing spark trail illusion revealed! ⚡️ #spark #tech #magic #viral",
            channel = techChannel,
            views = "16.8M",
            likesCount = "1.4M",
            commentsCount = "21.2K",
            audioTrackName = "Electric Dreams - SynthWave Original",
            imageResId = R.drawable.short_neon_trick,
            isLiked = false
        ),
        ShortItem(
            id = "short_2",
            title = "Cat's reaction to 8D audio bass drop will melt your heart! 🎧🐱 #catsoftiktok #cute #spark",
            channel = foodChannel,
            views = "10.4M",
            likesCount = "1.1M",
            commentsCount = "14.8K",
            audioTrackName = "Electro Beats Drop - DJ Whiskers",
            imageResId = R.drawable.short_cat_reaction,
            isLiked = true
        ),
        ShortItem(
            id = "short_3",
            title = "Waking up above the clouds in Hunza at sunrise 🏔️✨ #nature #pakistan #cinematic",
            channel = travelChannel,
            views = "6.7M",
            likesCount = "780K",
            commentsCount = "9.1K",
            audioTrackName = "Acoustic Mountain Horizon - Ambient Sound",
            imageResId = R.drawable.thumb_travel_mountains,
            isLiked = false
        ),
        ShortItem(
            id = "short_4",
            title = "How 5-star chefs glaze and layer Dum Biryani without drying the chicken! 🔥 #cookinghacks",
            channel = foodChannel,
            views = "4.2M",
            likesCount = "450K",
            commentsCount = "5.6K",
            audioTrackName = "Traditional Sitar & Tabla Fusion",
            imageResId = R.drawable.thumb_biryani_recipe,
            isLiked = false
        )
    )

    private val initialPlaylists = listOf(
        Playlist(id = "pl_liked", title = "Liked videos", videoCount = 18, thumbnailResId = R.drawable.thumb_tech_review, isPrivate = true),
        Playlist(id = "pl_watchlater", title = "Watch Later", videoCount = 8, thumbnailResId = R.drawable.thumb_biryani_recipe, isPrivate = true),
        Playlist(id = "pl_ai", title = "Modern Software & AI", videoCount = 24, thumbnailResId = R.drawable.thumb_gaming_epic, isPrivate = false),
        Playlist(id = "pl_chill", title = "Neon Coding Vibes", videoCount = 40, thumbnailResId = R.drawable.thumb_travel_mountains, isPrivate = false)
    )

    private val initialNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            title = "TechSpark Pro dropped: Quantum Smartphone Teardown",
            subtitle = "Thermal benchmarks and teardown are live now",
            timestamp = "1 hour ago",
            avatarColorHex = 0xFF0077FF,
            isRead = false
        ),
        NotificationItem(
            id = "notif_2",
            title = "Hamza Ali liked your comment: 'The thermal benchmarking is insane!'",
            subtitle = "Your comment reached 1,420 likes",
            timestamp = "3 hours ago",
            avatarColorHex = 0xFF00E5FF,
            isRead = false
        ),
        NotificationItem(
            id = "notif_3",
            title = "CyberNova Gaming is LIVE: Championship Final Defuse",
            subtitle = "Over 140,000 spectators streaming now",
            timestamp = "1 day ago",
            avatarColorHex = 0xFF023E8A,
            isRead = true
        )
    )

    private val _videos = MutableStateFlow(initialVideos)
    val videos: StateFlow<List<Video>> = _videos.asStateFlow()

    private val _userUploadedVideos = MutableStateFlow<List<Video>>(emptyList())
    val userUploadedVideos: StateFlow<List<Video>> = _userUploadedVideos.asStateFlow()

    private val _downloadedVideos = MutableStateFlow<List<Video>>(emptyList())
    val downloadedVideos: StateFlow<List<Video>> = _downloadedVideos.asStateFlow()

    private val _shorts = MutableStateFlow(initialShorts)
    val shorts: StateFlow<List<ShortItem>> = _shorts.asStateFlow()

    private val _watchHistory = MutableStateFlow<List<Video>>(listOf(initialVideos[0], initialVideos[1]))
    val watchHistory: StateFlow<List<Video>> = _watchHistory.asStateFlow()

    private val _playlists = MutableStateFlow(initialPlaylists)
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    private val _notifications = MutableStateFlow(initialNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    val subscribedChannels: List<Channel>
        get() = listOf(techChannel, travelChannel, musicChannel, foodChannel, gamingChannel, podcastsChannel)

    fun toggleVideoLike(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    val newLiked = !video.isLiked
                    val newCount = if (newLiked) video.likesCount + 1 else maxOf(0, video.likesCount - 1)
                    video.copy(isLiked = newLiked, likesCount = newCount, isDisliked = false)
                } else video
            }
        }
    }

    fun toggleVideoDislike(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    val newDisliked = !video.isDisliked
                    val newLikes = if (video.isLiked && newDisliked) maxOf(0, video.likesCount - 1) else video.likesCount
                    video.copy(isDisliked = newDisliked, isLiked = if (newDisliked) false else video.isLiked, likesCount = newLikes)
                } else video
            }
        }
    }

    fun toggleWatchLater(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    video.copy(isSavedToWatchLater = !video.isSavedToWatchLater)
                } else video
            }
        }
    }

    fun toggleDownload(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    val newDownloaded = !video.isDownloaded
                    val updated = video.copy(isDownloaded = newDownloaded)
                    if (newDownloaded) {
                        _downloadedVideos.update { current -> listOf(updated) + current.filterNot { it.id == videoId } }
                    } else {
                        _downloadedVideos.update { current -> current.filterNot { it.id == videoId } }
                    }
                    updated
                } else video
            }
        }
    }

    fun toggleChannelSubscribe(channelId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.channel.id == channelId) {
                    val updatedChannel = video.channel.copy(isSubscribed = !video.channel.isSubscribed)
                    video.copy(channel = updatedChannel)
                } else video
            }
        }
    }

    fun addToHistory(video: Video) {
        _watchHistory.update { history ->
            val filtered = history.filterNot { it.id == video.id }
            listOf(video) + filtered
        }
    }

    fun clearHistory() {
        _watchHistory.value = emptyList()
    }

    fun addComment(videoId: String, commentText: String) {
        val newComment = Comment(
            id = "c_${System.currentTimeMillis()}",
            authorName = "Bilal Muhammad",
            authorHandle = "@bilal_spark",
            authorColorHex = 0xFF0077FF,
            timestamp = "Just now",
            text = commentText,
            likesCount = 0
        )
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    video.copy(
                        commentsCount = video.commentsCount + 1,
                        topComment = newComment
                    )
                } else video
            }
        }
    }

    fun toggleShortLike(shortId: String) {
        _shorts.update { list ->
            list.map { item ->
                if (item.id == shortId) {
                    item.copy(isLiked = !item.isLiked)
                } else item
            }
        }
    }

    fun createNewPlaylist(title: String) {
        val newPl = Playlist(
            id = "pl_${System.currentTimeMillis()}",
            title = title,
            videoCount = 0,
            thumbnailResId = R.drawable.ic_spark_logo,
            isPrivate = true
        )
        _playlists.update { listOf(newPl) + it }
    }

    /**
     * Publishes a new video created by user.
     * Guaranteed to publish immediately and appear at the very top of Home Feed & "Your Videos" library!
     */
    fun publishNewVideo(
        title: String,
        category: String,
        description: String,
        thumbnailRes: Int = R.drawable.thumb_tech_review
    ): Video {
        val myChannel = Channel(
            id = "ch_my_spark",
            name = "Bilal Muhammad",
            handle = "@bilal_spark",
            subscribers = "340",
            avatarColorHex = 0xFF0077FF,
            isVerified = true,
            isSubscribed = true
        )
        val published = Video(
            id = "pub_${System.currentTimeMillis()}",
            title = title,
            description = description.ifBlank { "Published via Spark Studio. High definition streaming.\n\n#spark #creators #trending" },
            channel = myChannel,
            views = "1 view",
            uploadDate = "Just now",
            duration = "06:40",
            durationSeconds = 400,
            thumbnailResId = thumbnailRes,
            category = category,
            likesCount = 1,
            isLiked = true,
            commentsCount = 0
        )
        _videos.update { listOf(published) + it }
        _userUploadedVideos.update { listOf(published) + it }
        return published
    }

    /**
     * Imports a real YouTube video via YouTube oEmbed API or adds an imported video.
     */
    suspend fun importYouTubeVideo(urlOrId: String): Video? {
        val resolved = YouTubeOnlineService.fetchYouTubeVideoByUrl(urlOrId)
        if (resolved != null) {
            _videos.update { listOf(resolved) + it }
        }
        return resolved
    }
}
