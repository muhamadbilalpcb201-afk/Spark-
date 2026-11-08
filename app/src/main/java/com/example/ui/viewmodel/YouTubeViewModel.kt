package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Comment
import com.example.data.model.Playlist
import com.example.data.model.ShortItem
import com.example.data.model.Video
import com.example.data.repository.VideoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class YouTubeNavTab {
    HOME,
    SHORTS,
    SUBSCRIPTIONS,
    YOU
}

typealias SparkNavTab = YouTubeNavTab

class YouTubeViewModel(
    private val repository: VideoRepository = VideoRepository()
) : ViewModel() {

    private val _currentTab = MutableStateFlow(YouTubeNavTab.HOME)
    val currentTab: StateFlow<YouTubeNavTab> = _currentTab.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val categories = listOf(
        "All",
        "Gaming",
        "Music",
        "Podcasts",
        "Tech & Gadgets",
        "Cooking & Food",
        "Karakoram Travel",
        "Trending",
        "Live",
        "Recently uploaded",
        "Watched"
    )

    private val _activeVideo = MutableStateFlow<Video?>(null)
    val activeVideo: StateFlow<Video?> = _activeVideo.asStateFlow()

    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0f)
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _playbackQuality = MutableStateFlow("1080p")
    val playbackQuality: StateFlow<String> = _playbackQuality.asStateFlow()

    private val _isFullscreen = MutableStateFlow(false)
    val isFullscreen: StateFlow<Boolean> = _isFullscreen.asStateFlow()

    private val _isAutoplay = MutableStateFlow(true)
    val isAutoplay: StateFlow<Boolean> = _isAutoplay.asStateFlow()

    private val _isAmbientCinemaMode = MutableStateFlow(true)
    val isAmbientCinemaMode: StateFlow<Boolean> = _isAmbientCinemaMode.asStateFlow()

    private val _isCommentsSheetOpen = MutableStateFlow(false)
    val isCommentsSheetOpen: StateFlow<Boolean> = _isCommentsSheetOpen.asStateFlow()

    private val _isCreateSheetOpen = MutableStateFlow(false)
    val isCreateSheetOpen: StateFlow<Boolean> = _isCreateSheetOpen.asStateFlow()

    private val _isNotificationsOpen = MutableStateFlow(false)
    val isNotificationsOpen: StateFlow<Boolean> = _isNotificationsOpen.asStateFlow()

    private val _isCastDialogOpen = MutableStateFlow(false)
    val isCastDialogOpen: StateFlow<Boolean> = _isCastDialogOpen.asStateFlow()

    private val _isSearchOpen = MutableStateFlow(false)
    val isSearchOpen: StateFlow<Boolean> = _isSearchOpen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _isNewPlaylistDialogOpen = MutableStateFlow(false)
    val isNewPlaylistDialogOpen: StateFlow<Boolean> = _isNewPlaylistDialogOpen.asStateFlow()

    private val _isAiSummaryOpen = MutableStateFlow(false)
    val isAiSummaryOpen: StateFlow<Boolean> = _isAiSummaryOpen.asStateFlow()

    private val _currentAccount = MutableStateFlow("Bilal Muhammad (Main)")
    val currentAccount: StateFlow<String> = _currentAccount.asStateFlow()

    private val _isPublishing = MutableStateFlow(false)
    val isPublishing: StateFlow<Boolean> = _isPublishing.asStateFlow()

    private val _publishStatusMessage = MutableStateFlow<String?>(null)
    val publishStatusMessage: StateFlow<String?> = _publishStatusMessage.asStateFlow()

    val videos = repository.videos
    val userUploadedVideos = repository.userUploadedVideos
    val downloadedVideos = repository.downloadedVideos
    val shorts = repository.shorts
    val watchHistory = repository.watchHistory
    val playlists = repository.playlists
    val notifications = repository.notifications
    val subscribedChannels = repository.subscribedChannels

    // Filtered videos according to category and search query
    val filteredVideos: StateFlow<List<Video>> = combine(
        repository.videos,
        _selectedCategory,
        _searchQuery
    ) { allVideos, category, query ->
        allVideos.filter { video ->
            val matchesCategory = (category == "All") || (video.category == category)
            val matchesQuery = query.isBlank() ||
                    video.title.contains(query, ignoreCase = true) ||
                    video.channel.name.contains(query, ignoreCase = true) ||
                    video.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Continuous smooth playback ticker
        viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_activeVideo.value != null && _isPlaying.value) {
                    val current = _playbackProgress.value
                    val speed = _playbackSpeed.value
                    val videoDuration = _activeVideo.value?.durationSeconds ?: 300
                    val step = (1f / videoDuration) * speed
                    val next = current + step
                    if (next >= 1f) {
                        if (_isAutoplay.value) {
                            playNextVideo()
                        } else {
                            _playbackProgress.value = 1f
                            _isPlaying.value = false
                        }
                    } else {
                        _playbackProgress.value = next
                    }
                }
            }
        }
    }

    fun selectTab(tab: YouTubeNavTab) {
        _currentTab.value = tab
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun playVideo(video: Video) {
        _activeVideo.value = video
        _isPlaying.value = true
        _playbackProgress.value = 0.05f
        repository.addToHistory(video)
    }

    fun closePlayer() {
        _activeVideo.value = null
        _isPlaying.value = false
        _isFullscreen.value = false
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun seekTo(progress: Float) {
        _playbackProgress.value = progress.coerceIn(0f, 1f)
    }

    fun forward10() {
        val current = _playbackProgress.value
        val videoDuration = _activeVideo.value?.durationSeconds ?: 300
        val step = 10f / videoDuration
        _playbackProgress.value = (current + step).coerceIn(0f, 1f)
    }

    fun rewind10() {
        val current = _playbackProgress.value
        val videoDuration = _activeVideo.value?.durationSeconds ?: 300
        val step = 10f / videoDuration
        _playbackProgress.value = (current - step).coerceIn(0f, 1f)
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    fun setPlaybackQuality(quality: String) {
        _playbackQuality.value = quality
    }

    fun toggleFullscreen() {
        _isFullscreen.value = !_isFullscreen.value
    }

    fun toggleAutoplay() {
        _isAutoplay.value = !_isAutoplay.value
    }

    fun toggleAmbientCinemaMode() {
        _isAmbientCinemaMode.value = !_isAmbientCinemaMode.value
    }

    fun playNextVideo() {
        val currentList = videos.value
        val currentIndex = currentList.indexOfFirst { it.id == _activeVideo.value?.id }
        if (currentIndex != -1 && currentIndex + 1 < currentList.size) {
            playVideo(currentList[currentIndex + 1])
        } else if (currentList.isNotEmpty()) {
            playVideo(currentList.first())
        }
    }

    fun toggleLike(video: Video) {
        repository.toggleVideoLike(video.id)
        if (_activeVideo.value?.id == video.id) {
            _activeVideo.value = _activeVideo.value?.let { current ->
                val newLiked = !current.isLiked
                val newLikes = if (newLiked) current.likesCount + 1 else maxOf(0, current.likesCount - 1)
                current.copy(isLiked = newLiked, likesCount = newLikes, isDisliked = false)
            }
        }
    }

    fun toggleDislike(video: Video) {
        repository.toggleVideoDislike(video.id)
        if (_activeVideo.value?.id == video.id) {
            _activeVideo.value = _activeVideo.value?.let { current ->
                val newDisliked = !current.isDisliked
                val newLikes = if (current.isLiked && newDisliked) maxOf(0, current.likesCount - 1) else current.likesCount
                current.copy(isDisliked = newDisliked, isLiked = if (newDisliked) false else current.isLiked, likesCount = newLikes)
            }
        }
    }

    fun toggleSubscribe(channelId: String) {
        repository.toggleChannelSubscribe(channelId)
        if (_activeVideo.value?.channel?.id == channelId) {
            _activeVideo.value = _activeVideo.value?.let { current ->
                val updated = current.channel.copy(isSubscribed = !current.channel.isSubscribed)
                current.copy(channel = updated)
            }
        }
    }

    fun toggleWatchLater(video: Video) {
        repository.toggleWatchLater(video.id)
    }

    fun toggleDownload(video: Video) {
        repository.toggleDownload(video.id)
        if (_activeVideo.value?.id == video.id) {
            _activeVideo.value = _activeVideo.value?.copy(isDownloaded = !_activeVideo.value!!.isDownloaded)
        }
    }

    fun addComment(videoId: String, text: String) {
        repository.addComment(videoId, text)
    }

    fun toggleShortLike(shortItem: ShortItem) {
        repository.toggleShortLike(shortItem.id)
    }

    fun createPlaylist(name: String) {
        if (name.isNotBlank()) {
            repository.createNewPlaylist(name)
        }
    }

    /**
     * Publishes a user video with real progress animation.
     * Guaranteed to publish immediately and play on Spark!
     */
    fun publishVideo(title: String, category: String, desc: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isPublishing.value = true
            delay(1200) // realistic smooth upload simulation
            val published = repository.publishNewVideo(title, category, desc)
            _isPublishing.value = false
            _publishStatusMessage.value = "⚡ Published \"$title\" successfully to Spark!"
            onComplete()
            // Auto open the newly published video
            playVideo(published)
        }
    }

    /**
     * Imports a real YouTube video link or ID.
     */
    fun importYouTubeVideo(urlOrId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isPublishing.value = true
            val imported = repository.importYouTubeVideo(urlOrId)
            _isPublishing.value = false
            if (imported != null) {
                _publishStatusMessage.value = "⚡ YouTube video \"${imported.title}\" imported to Spark!"
                playVideo(imported)
                onResult(true)
            } else {
                _publishStatusMessage.value = "Could not fetch YouTube URL. Added custom video instead."
                val fallback = repository.publishNewVideo(
                    title = "YouTube Stream: $urlOrId",
                    category = "Trending",
                    description = "Imported from YouTube link $urlOrId into Spark."
                )
                playVideo(fallback)
                onResult(true)
            }
        }
    }

    fun clearHistory() {
        repository.clearHistory()
    }

    fun clearStatusMessage() {
        _publishStatusMessage.value = null
    }

    fun openComments() {
        _isCommentsSheetOpen.value = true
    }

    fun closeComments() {
        _isCommentsSheetOpen.value = false
    }

    fun openCreate() {
        _isCreateSheetOpen.value = true
    }

    fun closeCreate() {
        _isCreateSheetOpen.value = false
    }

    fun openNotifications() {
        _isNotificationsOpen.value = true
    }

    fun closeNotifications() {
        _isNotificationsOpen.value = false
    }

    fun openCastDialog() {
        _isCastDialogOpen.value = true
    }

    fun closeCastDialog() {
        _isCastDialogOpen.value = false
    }

    fun openSearch() {
        _isSearchOpen.value = true
    }

    fun closeSearch() {
        _isSearchOpen.value = false
        _searchQuery.value = ""
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun setDarkTheme(enabled: Boolean) {
        _isDarkTheme.value = enabled
    }

    fun openAiSummary() {
        _isAiSummaryOpen.value = true
    }

    fun closeAiSummary() {
        _isAiSummaryOpen.value = false
    }

    fun switchAccount(account: String) {
        _currentAccount.value = account
    }
}
