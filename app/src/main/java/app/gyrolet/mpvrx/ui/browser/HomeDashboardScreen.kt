/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Stage 2 — local-first cinematic home experience for mpvRx.
 * No online catalogue is used here: every card is derived from local media,
 * Room playback/favourite/playlist state, or device storage information.
 */
package app.gyrolet.mpvrx.ui.browser

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.gyrolet.mpvrx.database.entities.PlaybackStateEntity
import app.gyrolet.mpvrx.database.entities.PlaylistEntity
import app.gyrolet.mpvrx.database.entities.PlaylistItemEntity
import app.gyrolet.mpvrx.database.entities.RecentlyPlayedEntity
import app.gyrolet.mpvrx.database.repository.PlaylistRepository
import app.gyrolet.mpvrx.domain.media.model.Video
import app.gyrolet.mpvrx.domain.media.model.VideoFolder
import app.gyrolet.mpvrx.domain.playbackstate.repository.PlaybackStateRepository
import app.gyrolet.mpvrx.domain.recentlyplayed.repository.RecentlyPlayedRepository
import app.gyrolet.mpvrx.domain.thumbnail.ThumbnailRepository
import app.gyrolet.mpvrx.repository.MediaFileRepository
import app.gyrolet.mpvrx.ui.browser.playlist.PlaylistScreen
import app.gyrolet.mpvrx.ui.browser.videolist.VideoListScreen
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons
import app.gyrolet.mpvrx.ui.player.controls.components.tvFocusHighlight
import app.gyrolet.mpvrx.ui.theme.CinematicDesign
import app.gyrolet.mpvrx.ui.theme.CinematicPulse
import app.gyrolet.mpvrx.ui.utils.LocalBackStack
import app.gyrolet.mpvrx.ui.utils.navigateTo
import app.gyrolet.mpvrx.utils.media.MediaUtils
import app.gyrolet.mpvrx.utils.storage.FileTypeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject
import java.io.File
import java.util.Locale
import kotlin.math.max

private val HomeBackground = CinematicDesign.Background
private val HomePanel = CinematicDesign.Surface
private val HomePanel2 = CinematicDesign.Surface2
private val HomePanel3 = CinematicDesign.Surface3
private val NeonPurple = CinematicDesign.Purple
private val NeonBlue = CinematicDesign.Blue
private val NeonPink = CinematicDesign.Pink
private val NeonOrange = CinematicDesign.Orange
private val NeonGreen = CinematicDesign.Green
private val TextMuted = CinematicDesign.TextMuted

private data class HomeData(
  val folders: List<VideoFolder> = emptyList(),
  val videos: List<Video> = emptyList(),
  val recent: List<RecentlyPlayedEntity> = emptyList(),
  val favorites: List<PlaylistItemEntity> = emptyList(),
  val playlists: List<PlaylistEntity> = emptyList(),
  val playback: List<PlaybackStateEntity> = emptyList(),
  val lastPlayed: RecentlyPlayedEntity? = null,
)

private enum class HomeFilter(val title: String) {
  ALL("الكل"),
  FOUR_K("4K"),
  HDR("HDR"),
  SUBTITLES("ترجمة"),
  LONG("طويل"),
  SHORT("قصير"),
}

private enum class LibraryMode { CARDS, COMPACT }

private enum class SortMode(val title: String) {
  NEWEST("الأحدث"),
  NAME("الاسم"),
  LONGEST("المدة"),
  LARGEST("الحجم"),
}

object HomeDashboardScreen {
  @Composable
  fun Content() {
    val context = LocalContext.current
    val backStack = LocalBackStack.current
    val thumbnailRepository = koinInject<ThumbnailRepository>()
    val recentlyPlayedRepository = koinInject<RecentlyPlayedRepository>()
    val playbackStateRepository = koinInject<PlaybackStateRepository>()
    val playlistRepository = koinInject<PlaylistRepository>()

    var data by remember { mutableStateOf(HomeData()) }
    var selectedFilter by rememberSaveable { mutableStateOf(HomeFilter.ALL) }
    var selectedHero by rememberSaveable { mutableStateOf(0) }
    var libraryMode by rememberSaveable { mutableStateOf(LibraryMode.CARDS) }
    var sortMode by rememberSaveable { mutableStateOf(SortMode.NEWEST) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showSearch by rememberSaveable { mutableStateOf(false) }
    var showFilters by rememberSaveable { mutableStateOf(false) }
    var showStats by rememberSaveable { mutableStateOf(false) }
    var refreshing by rememberSaveable { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    suspend fun refresh() {
      refreshing = true
      data = loadHomeData(context, recentlyPlayedRepository, playbackStateRepository, playlistRepository)
      selectedHero = 0
      refreshing = false
    }

    LaunchedEffect(Unit) { refresh() }

    val filteredVideos = remember(data.videos, selectedFilter, searchQuery, sortMode) {
      val filtered = filterHomeVideos(data.videos, selectedFilter).let { list ->
        if (searchQuery.isBlank()) list else list.filter {
          it.displayName.contains(searchQuery.trim(), ignoreCase = true) ||
            it.path.contains(searchQuery.trim(), ignoreCase = true)
        }
      }
      when (sortMode) {
        SortMode.NEWEST -> filtered.sortedByDescending { it.dateAdded }
        SortMode.NAME -> filtered.sortedBy { it.displayName.lowercase(Locale.ROOT) }
        SortMode.LONGEST -> filtered.sortedByDescending { it.duration }
        SortMode.LARGEST -> filtered.sortedByDescending { it.size }
      }
    }

    val heroItems = remember(data.lastPlayed, data.videos, data.playback) {
      buildList {
        data.lastPlayed?.let { recent ->
          if (recent.filePath.isNotBlank()) add(PreviewMedia.RecentPreview(recent))
        }
        data.videos.filter(::isValidLocalVideo).take(7).forEach { add(PreviewMedia.VideoPreview(it)) }
      }.distinctBy { it.path }
    }

    LaunchedEffect(heroItems.size) {
      if (heroItems.size > 1) {
        while (true) {
          delay(7000)
          selectedHero = (selectedHero + 1) % heroItems.size
        }
      }
    }

    val hero = heroItems.getOrNull(selectedHero.coerceAtMost((heroItems.size - 1).coerceAtLeast(0)))
    val resumeState = hero?.let { preview ->
      data.playback.firstOrNull { it.mediaTitle.equals(preview.title, ignoreCase = true) }
    }

    if (showFilters) {
      FilterDialog(
        selectedFilter = selectedFilter,
        selectedSort = sortMode,
        libraryMode = libraryMode,
        onFilter = { selectedFilter = it },
        onSort = { sortMode = it },
        onMode = { libraryMode = it },
        onDismiss = { showFilters = false },
      )
    }

    if (showStats) {
      LibraryStatsDialog(data = data, onDismiss = { showStats = false })
    }

    Box(modifier = Modifier.fillMaxSize().background(HomeBackground)) {
      Box(modifier = CinematicDesign.ambientModifier().fillMaxSize())
      LazyColumn(
      modifier = Modifier.fillMaxSize().background(Color.Transparent),
      contentPadding = PaddingValues(
        start = 14.dp,
        end = 14.dp,
        top = 10.dp,
        bottom = LocalNavigationBarHeight.current + 30.dp,
      ),
      verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
      item {
        StageTwoTopBar(
          searchVisible = showSearch,
          query = searchQuery,
          onQuery = { searchQuery = it },
          onSearch = { showSearch = !showSearch },
          onRefresh = { if (!refreshing) refreshScope.launch { refresh() } },
          onFilters = { showFilters = true },
          onStats = { showStats = true },
          onSettings = { backStack.navigateTo(app.gyrolet.mpvrx.ui.preferences.PreferencesScreen) },
        )
      }

      if (hero != null) {
        item {
          HeroCarousel(
            preview = hero,
            thumbnailRepository = thumbnailRepository,
            resumeState = resumeState,
            index = selectedHero,
            count = heroItems.size,
            onPlay = { MediaUtils.playFile(hero.path, context, "home_resume") },
            onPrevious = { if (heroItems.isNotEmpty()) selectedHero = (selectedHero - 1 + heroItems.size) % heroItems.size },
            onNext = { if (heroItems.isNotEmpty()) selectedHero = (selectedHero + 1) % heroItems.size },
          )
        }
      } else {
        item { EmptyLibraryHero(onSettings = { backStack.navigateTo(app.gyrolet.mpvrx.ui.preferences.PreferencesScreen) }) }
      }

      item {
        QuickActionRibbon(
          selectedFilter = selectedFilter,
          onFilter = { selectedFilter = it },
          onLibrary = { showFilters = true },
          onStats = { showStats = true },
          onPlaylists = { backStack.navigateTo(PlaylistScreen) },
        )
      }

      item { HomeSectionTitle("مكتبة الفيديو", "كل ملفاتك المحلية، بدون كتالوج إنترنت", Icons.RoundedFilled.VideoLibrary, NeonBlue) }
      item {
        FolderRail(
          folders = data.folders.take(14),
          thumbnailRepository = thumbnailRepository,
          context = context,
          onFolderClick = { folder -> backStack.navigateTo(VideoListScreen(folder.bucketId, folder.name, isAudio = false)) },
        )
      }

      item { HomeSectionTitle("🔥 أحداث المكتبة", "أحدث الملفات وآخر ما تم تشغيله", Icons.RoundedFilled.AutoAwesome, NeonOrange) }
      item {
        EventRail(
          recent = data.recent.take(12),
          thumbnailRepository = thumbnailRepository,
          context = context,
          playback = data.playback,
        )
      }

      item {
        HomeSectionTitle("🎬 مكتبة الفيديو", "تصفية وفرز وعرض حي للمحتوى", Icons.RoundedFilled.Movie, NeonPurple)
      }
      item {
        LibraryToolbar(
          filter = selectedFilter,
          sort = sortMode,
          mode = libraryMode,
          resultCount = filteredVideos.size,
          onFilters = { showFilters = true },
          onMode = { libraryMode = if (libraryMode == LibraryMode.CARDS) LibraryMode.COMPACT else LibraryMode.CARDS },
        )
      }
      item {
        AnimatedContent(targetState = libraryMode, label = "library-mode") { mode ->
          when (mode) {
            LibraryMode.CARDS -> VideoRail(filteredVideos.take(20), thumbnailRepository, context, "لا توجد ملفات مطابقة")
            LibraryMode.COMPACT -> CompactVideoGrid(filteredVideos.take(24), thumbnailRepository, context)
          }
        }
      }

      item { HomeSectionTitle("❤️ المفضلة", "ملفات محلية اخترتها أنت", Icons.RoundedFilled.Favorite, NeonPink) }
      item { PlaylistItemRail(data.favorites.take(14), thumbnailRepository, context) }

      item { HomeSectionTitle("▶ قوائم التشغيل", "قوائمك الحقيقية من داخل المشغل", Icons.RoundedFilled.PlaylistPlay, NeonPurple) }
      item {
        PlaylistRail(
          playlists = data.playlists.filter { !it.name.equals(PlaylistRepository.FAVORITES_PLAYLIST_NAME, true) }.take(12),
          onClick = { backStack.navigateTo(PlaylistScreen) },
        )
      }

      item { LocalControlDeck(onSettings = { backStack.navigateTo(app.gyrolet.mpvrx.ui.preferences.PreferencesScreen) }) }
      item { StorageAndLibraryCard(context, data) }
      item {
        CinematicPulse(
          modifier = Modifier.fillMaxWidth().height(8.dp),
          accent = NeonPurple,
        )
      }
    }
    }
  }
}

private suspend fun loadHomeData(
  context: Context,
  recentlyPlayedRepository: RecentlyPlayedRepository,
  playbackStateRepository: PlaybackStateRepository,
  playlistRepository: PlaylistRepository,
): HomeData = withContext(Dispatchers.IO) {
  val folders = runCatching { MediaFileRepository.getAllVideoFolders(context) }.getOrDefault(emptyList())
  val videos = runCatching {
    MediaFileRepository.getAllVideos(context, includeAudioOverride = false)
      .filter { !it.isAudio }
      .distinctBy { it.path }
      .sortedByDescending { it.dateAdded }
  }.getOrDefault(emptyList())

  val recent = runCatching { recentlyPlayedRepository.getRecentlyPlayed(40) }.getOrDefault(emptyList())
  val lastPlayed = recent.firstOrNull()
  val playback = runCatching { playbackStateRepository.getAllPlaybackStates() }.getOrDefault(emptyList())
  val playlists = runCatching { playlistRepository.getAllPlaylists(false) }.getOrDefault(emptyList())
  val favoritePlaylist = playlists.firstOrNull {
    it.name.equals(PlaylistRepository.FAVORITES_PLAYLIST_NAME, ignoreCase = true)
  }
  val favorites = favoritePlaylist?.let {
    runCatching { playlistRepository.getPlaylistItems(it.id) }.getOrDefault(emptyList())
  } ?: emptyList()

  HomeData(folders, videos, recent, favorites, playlists, playback, lastPlayed)
}

private fun filterHomeVideos(videos: List<Video>, filter: HomeFilter): List<Video> = when (filter) {
  HomeFilter.ALL -> videos
  HomeFilter.FOUR_K -> videos.filter { it.width >= 3840 || it.height >= 2160 }
  HomeFilter.HDR -> videos.filter {
    it.mimeType.contains("hdr", true) || it.videoCodec.contains("hdr", true)
  }
  HomeFilter.SUBTITLES -> videos.filter { it.hasEmbeddedSubtitles }
  HomeFilter.LONG -> videos.filter { it.duration >= 45 * 60 * 1000L }
  HomeFilter.SHORT -> videos.filter { it.duration in 1..(15 * 60 * 1000L) }
}

private fun isValidLocalVideo(video: Video): Boolean =
  video.path.isNotBlank() && !video.path.startsWith("http", true) && !video.uri.toString().startsWith("http", true)

private sealed class PreviewMedia {
  abstract val path: String
  abstract val title: String
  abstract val duration: Long

  data class VideoPreview(val video: Video) : PreviewMedia() {
    override val path get() = video.path
    override val title get() = video.displayName
    override val duration get() = video.duration
  }

  data class RecentPreview(val recent: RecentlyPlayedEntity) : PreviewMedia() {
    override val path get() = recent.filePath
    override val title get() = recent.videoTitle ?: recent.fileName
    override val duration get() = recent.duration
  }

  fun asVideo(): Video = when (this) {
    is VideoPreview -> video
    is RecentPreview -> localVideoFromPath(path, title).copy(duration = duration)
  }
}

@Composable
private fun StageTwoTopBar(
  searchVisible: Boolean,
  query: String,
  onQuery: (String) -> Unit,
  onSearch: () -> Unit,
  onRefresh: () -> Unit,
  onFilters: () -> Unit,
  onStats: () -> Unit,
  onSettings: () -> Unit,
) {
  Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(7.dp),
    ) {
      Surface(
        shape = RoundedCornerShape(22.dp),
        color = HomePanel,
        border = BorderStroke(1.dp, NeonPurple.copy(.32f)),
      ) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
          Box(
            Modifier.size(38.dp).clip(CircleShape).background(Brush.linearGradient(listOf(NeonPurple, NeonBlue))),
            contentAlignment = Alignment.Center,
          ) { Icon(Icons.RoundedFilled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(24.dp)) }
          Spacer(Modifier.width(9.dp))
          Column {
            Text("mpvRx", color = Color.White, fontWeight = FontWeight.ExtraBold)
            Text("LOCAL • CINEMA", color = TextMuted, style = MaterialTheme.typography.labelSmall)
          }
        }
      }
      Spacer(Modifier.weight(1f))
      TopIcon(Icons.RoundedFilled.Search, "البحث", onSearch)
      TopIcon(Icons.RoundedFilled.Tune, "الفلاتر", onFilters)
      TopIcon(Icons.RoundedFilled.Info, "إحصائيات", onStats)
      TopIcon(Icons.RoundedFilled.Refresh, "تحديث", onRefresh)
      TopIcon(Icons.RoundedFilled.Settings, "الإعدادات", onSettings)
    }
    AnimatedVisibility(searchVisible, enter = fadeIn() + scaleIn(), exit = fadeOut()) {
      Surface(
        shape = RoundedCornerShape(18.dp),
        color = HomePanel,
        border = BorderStroke(1.dp, NeonBlue.copy(.28f)),
        modifier = Modifier.fillMaxWidth(),
      ) {
        androidx.compose.material3.OutlinedTextField(
          value = query,
          onValueChange = onQuery,
          modifier = Modifier.fillMaxWidth(),
          singleLine = true,
          placeholder = { Text("ابحث في أسماء الفيديوهات والمجلدات المحلية…", color = TextMuted) },
          leadingIcon = { Icon(Icons.RoundedFilled.Search, null, tint = NeonBlue) },
        )
      }
    }
  }
}

@Composable
private fun TopIcon(icon: app.gyrolet.mpvrx.ui.icons.AppIcon, description: String, onClick: () -> Unit) {
  Surface(
    onClick = onClick,
    shape = CircleShape,
    color = HomePanel,
    border = BorderStroke(1.dp, Color.White.copy(.08f)),
  ) { Icon(icon, description, tint = Color.White, modifier = Modifier.padding(10.dp).size(20.dp)) }
}

@Composable
private fun HeroCarousel(
  preview: PreviewMedia,
  thumbnailRepository: ThumbnailRepository,
  resumeState: PlaybackStateEntity?,
  index: Int,
  count: Int,
  onPlay: () -> Unit,
  onPrevious: () -> Unit,
  onNext: () -> Unit,
) {
  val pulse by rememberInfiniteTransition(label = "hero-pulse").animateFloat(
    initialValue = .92f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), androidx.compose.animation.core.RepeatMode.Reverse),
    label = "hero-pulse-value",
  )
  val isResume = resumeState != null && resumeState.mediaTitle.equals(preview.title, true)
  val progress = if (isResume && preview.duration > 0) {
    (resumeState.lastPosition.toFloat() / (preview.duration / 1000f)).coerceIn(0f, 1f)
  } else 0f

  Box(
    Modifier.fillMaxWidth().height(285.dp)
      .clip(RoundedCornerShape(30.dp))
      .border(1.dp, Brush.linearGradient(listOf(NeonPurple.copy(.65f), NeonBlue.copy(.25f))), RoundedCornerShape(30.dp))
      .shadow(18.dp, RoundedCornerShape(30.dp)),
  ) {
    AnimatedContent(targetState = preview.path, transitionSpec = { fadeIn(tween(380)) togetherWith fadeOut(tween(260)) }, label = "hero-content") { path ->
      ThumbnailImage(preview.asVideo(), thumbnailRepository, Modifier.fillMaxSize(), 16f / 9f, HomePanel2)
    }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent, HomeBackground.copy(.98f)))))
    Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(HomeBackground.copy(.88f), Color.Transparent, Color.Transparent))))

    Column(Modifier.align(Alignment.BottomStart).padding(18.dp).fillMaxWidth(.82f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        HeroBadge(if (isResume) "متابعة" else "من مكتبتك", if (isResume) NeonPurple else NeonBlue)
        HeroBadge("محلي", NeonGreen)
        if (preview.asVideo().width >= 3840 || preview.asVideo().height >= 2160) HeroBadge("4K", NeonOrange)
        if (preview.asVideo().hasEmbeddedSubtitles) HeroBadge("CC", NeonBlue)
      }
      Text(preview.title, color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
      Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        Text(formatDuration(preview.duration), color = TextMuted, style = MaterialTheme.typography.labelSmall)
        if (isResume) Text("• ${formatSeconds(resumeState.lastPosition)} شاهد", color = TextMuted, style = MaterialTheme.typography.labelSmall)
      }
      if (isResume) {
        Box(Modifier.fillMaxWidth().height(5.dp).clip(CircleShape).background(Color.White.copy(.14f))) {
          Box(Modifier.fillMaxWidth(progress).fillMaxHeight().clip(CircleShape).background(Brush.horizontalGradient(listOf(NeonPurple, NeonBlue))))
        }
      }
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(onClick = onPlay, shape = RoundedCornerShape(14.dp), color = NeonPurple, shadowElevation = 10.dp) {
          Row(Modifier.padding(horizontal = 17.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.RoundedFilled.PlayArrow, null, tint = Color.White)
            Spacer(Modifier.width(6.dp))
            Text(if (isResume) "متابعة المشاهدة" else "تشغيل", color = Color.White, fontWeight = FontWeight.Bold)
          }
        }
        Surface(onClick = onPrevious, shape = CircleShape, color = Color.Black.copy(.45f), border = BorderStroke(1.dp, Color.White.copy(.12f))) {
          Icon(Icons.RoundedFilled.ChevronLeft, null, tint = Color.White, modifier = Modifier.padding(9.dp).size(20.dp))
        }
        Surface(onClick = onNext, shape = CircleShape, color = Color.Black.copy(.45f), border = BorderStroke(1.dp, Color.White.copy(.12f))) {
          Icon(Icons.RoundedFilled.ChevronRight, null, tint = Color.White, modifier = Modifier.padding(9.dp).size(20.dp))
        }
      }
    }

    Column(Modifier.align(Alignment.TopEnd).padding(12.dp), horizontalAlignment = Alignment.End) {
      Text("${index + 1} / $count", color = Color.White.copy(.88f), style = MaterialTheme.typography.labelSmall)
      Spacer(Modifier.height(5.dp))
      Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(count.coerceAtMost(8)) { dot ->
          val selected = dot == index.coerceAtMost(7)
          Box(Modifier.size(if (selected) 18.dp else 6.dp, 6.dp).clip(CircleShape).background(if (selected) NeonPurple else Color.White.copy(.35f)))
        }
      }
    }

    Box(Modifier.align(Alignment.TopStart).padding(13.dp).size((42 * pulse).dp).clip(CircleShape).background(NeonPurple.copy(.12f))) {
      Icon(Icons.RoundedFilled.AutoAwesome, null, tint = NeonPurple, modifier = Modifier.align(Alignment.Center).size(19.dp))
    }
  }
}

@Composable
private fun HeroBadge(text: String, color: Color) {
  Surface(shape = RoundedCornerShape(8.dp), color = color.copy(.18f), border = BorderStroke(1.dp, color.copy(.35f))) {
    Text(text, color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp))
  }
}

@Composable
private fun EmptyLibraryHero(onSettings: () -> Unit) {
  Surface(Modifier.fillMaxWidth().height(235.dp), shape = RoundedCornerShape(30.dp), color = HomePanel, border = BorderStroke(1.dp, NeonPurple.copy(.22f))) {
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
      Icon(Icons.RoundedFilled.VideoLibrary, null, tint = NeonPurple, modifier = Modifier.size(52.dp))
      Spacer(Modifier.height(10.dp))
      Text("مكتبتك المحلية جاهزة", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
      Text("أضف مجلدات الفيديو من إعدادات المكتبة لتظهر الصور والمعلومات هنا.", color = TextMuted, textAlign = TextAlign.Center)
      Spacer(Modifier.height(12.dp))
      OutlinedButton(onClick = onSettings) { Text("إدارة المجلدات") }
    }
  }
}

@Composable
private fun QuickActionRibbon(
  selectedFilter: HomeFilter,
  onFilter: (HomeFilter) -> Unit,
  onLibrary: () -> Unit,
  onStats: () -> Unit,
  onPlaylists: () -> Unit,
) {
  LazyRow(horizontalArrangement = Arrangement.spacedBy(9.dp), contentPadding = PaddingValues(horizontal = 2.dp)) {
    item { ActionChip("المكتبة", Icons.RoundedFilled.VideoLibrary, NeonBlue, onLibrary) }
    item { ActionChip("المفضلة", Icons.RoundedFilled.Favorite, NeonPink, onStats) }
    item { ActionChip("قوائم التشغيل", Icons.RoundedFilled.PlaylistPlay, NeonPurple, onPlaylists) }
    item { ActionChip("4K", Icons.RoundedFilled.HdrOn, NeonOrange) { onFilter(HomeFilter.FOUR_K) } }
    item { ActionChip("ترجمة", Icons.RoundedFilled.Subtitles, NeonBlue) { onFilter(HomeFilter.SUBTITLES) } }
    item { ActionChip("HDR", Icons.RoundedFilled.HdrOn, NeonPink) { onFilter(HomeFilter.HDR) } }
    item { ActionChip("${selectedFilter.title}", Icons.RoundedFilled.Tune, NeonGreen, onLibrary) }
  }
}

@Composable
private fun ActionChip(title: String, icon: app.gyrolet.mpvrx.ui.icons.AppIcon, accent: Color, onClick: () -> Unit) {
  Surface(onClick = onClick, shape = RoundedCornerShape(17.dp), color = HomePanel, border = BorderStroke(1.dp, accent.copy(.25f))) {
    Row(Modifier.padding(horizontal = 12.dp, vertical = 9.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
      Icon(icon, null, tint = accent, modifier = Modifier.size(18.dp))
      Text(title, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
    }
  }
}

@Composable
private fun HomeSectionTitle(title: String, subtitle: String, icon: app.gyrolet.mpvrx.ui.icons.AppIcon, accent: Color) {
  Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    Box(Modifier.size(40.dp).clip(RoundedCornerShape(13.dp)).background(accent.copy(.12f)), contentAlignment = Alignment.Center) {
      Icon(icon, null, tint = accent, modifier = Modifier.size(21.dp))
    }
    Spacer(Modifier.width(9.dp))
    Column(Modifier.weight(1f)) {
      Text(title, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
      Text(subtitle, color = TextMuted, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
    Box(Modifier.width(34.dp).height(2.dp).background(Brush.horizontalGradient(listOf(accent, Color.Transparent))))
  }
}

@Composable
private fun FolderRail(
  folders: List<VideoFolder>,
  thumbnailRepository: ThumbnailRepository,
  context: Context,
  onFolderClick: (VideoFolder) -> Unit,
) {
  if (folders.isEmpty()) {
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), color = HomePanel) { Text("لا توجد مجلدات فيديو مكتشفة بعد", color = TextMuted, modifier = Modifier.padding(20.dp)) }
    return
  }
  LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 2.dp)) {
    items(folders, key = { it.bucketId }) { folder -> FolderHomeCard(folder, thumbnailRepository, context, onFolderClick) }
  }
}

@Composable
private fun FolderHomeCard(folder: VideoFolder, repository: ThumbnailRepository, context: Context, onClick: (VideoFolder) -> Unit) {
  var videos by remember(folder.bucketId) { mutableStateOf<List<Video>>(emptyList()) }
  LaunchedEffect(folder.bucketId) { videos = MediaFileRepository.getVideosInFolder(context, folder.bucketId).take(5) }
  val cover = videos.firstOrNull()
  Surface(onClick = { onClick(folder) }, modifier = Modifier.width(214.dp).height(190.dp).tvFocusHighlight(RoundedCornerShape(24.dp), 1.035f), shape = RoundedCornerShape(24.dp), color = HomePanel, border = BorderStroke(1.dp, NeonBlue.copy(.18f))) {
    Box {
      if (cover != null) ThumbnailImage(cover, repository, Modifier.fillMaxSize(), 16f / 9f, HomePanel2) else Box(Modifier.fillMaxSize().background(HomePanel2))
      Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, HomeBackground.copy(.98f)))))
      Surface(Modifier.align(Alignment.TopEnd).padding(9.dp), color = Color.Black.copy(.55f), shape = RoundedCornerShape(9.dp)) {
        Text("${folder.videoCount} فيديو", color = Color.White, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp))
      }
      Column(Modifier.align(Alignment.BottomStart).padding(12.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.RoundedFilled.Folder, null, tint = NeonBlue, modifier = Modifier.size(20.dp))
          Spacer(Modifier.width(6.dp))
          Text(folder.name, color = Color.White, fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text("${formatBytes(folder.totalSize)}  •  ${formatDuration(folder.totalDuration)}", color = TextMuted, style = MaterialTheme.typography.labelSmall)
        if (videos.isNotEmpty()) Text("آخر ملف: ${videos.first().displayName}", color = TextMuted.copy(.85f), style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
      }
    }
  }
}

@Composable
private fun EventRail(
  recent: List<RecentlyPlayedEntity>,
  thumbnailRepository: ThumbnailRepository,
  context: Context,
  playback: List<PlaybackStateEntity>,
) {
  if (recent.isEmpty()) {
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), color = HomePanel) { Text("ابدأ تشغيل فيديو محلي وسيظهر سجل الأحداث هنا.", color = TextMuted, modifier = Modifier.padding(20.dp)) }
    return
  }
  LazyRow(horizontalArrangement = Arrangement.spacedBy(11.dp), contentPadding = PaddingValues(horizontal = 2.dp)) {
    items(recent, key = { it.id }) { item ->
      val state = playback.firstOrNull { it.mediaTitle.equals(item.videoTitle ?: item.fileName, true) }
      EventCard(item, state, thumbnailRepository, context)
    }
  }
}

@Composable
private fun EventCard(item: RecentlyPlayedEntity, state: PlaybackStateEntity?, repository: ThumbnailRepository, context: Context) {
  val video = remember(item.filePath) { localVideoFromPath(item.filePath, item.videoTitle ?: item.fileName).copy(duration = item.duration) }
  Surface(onClick = { MediaUtils.playFile(item.filePath, context, "home_recent") }, modifier = Modifier.width(190.dp).height(152.dp), shape = RoundedCornerShape(21.dp), color = HomePanel, border = BorderStroke(1.dp, NeonOrange.copy(.17f))) {
    Box {
      ThumbnailImage(video, repository, Modifier.fillMaxSize(), 16f / 9f, HomePanel2)
      Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, HomeBackground.copy(.95f)))))
      Surface(Modifier.align(Alignment.TopStart).padding(8.dp), shape = CircleShape, color = NeonOrange.copy(.82f)) { Icon(Icons.RoundedFilled.History, null, tint = Color.White, modifier = Modifier.padding(6.dp).size(16.dp)) }
      Column(Modifier.align(Alignment.BottomStart).padding(10.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(item.videoTitle ?: item.fileName, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(if (state != null && state.lastPosition > 0) "متابعة من ${formatSeconds(state.lastPosition)}" else formatDuration(item.duration), color = TextMuted, style = MaterialTheme.typography.labelSmall)
      }
    }
  }
}

@Composable
private fun VideoRail(videos: List<Video>, repository: ThumbnailRepository, context: Context, emptyText: String) {
  if (videos.isEmpty()) {
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = HomePanel) { Text(emptyText, color = TextMuted, modifier = Modifier.padding(20.dp)) }
    return
  }
  LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 2.dp)) {
    items(videos, key = { it.path }) { video -> VideoHomeCard(video, repository, context) }
  }
}

@Composable
private fun VideoHomeCard(video: Video, repository: ThumbnailRepository, context: Context) {
  Surface(onClick = { MediaUtils.playFile(video, context, "home_library") }, modifier = Modifier.width(158.dp).height(226.dp).tvFocusHighlight(RoundedCornerShape(20.dp), 1.045f), shape = RoundedCornerShape(20.dp), color = HomePanel, border = BorderStroke(1.dp, NeonPurple.copy(.15f))) {
    Box {
      ThumbnailImage(video, repository, Modifier.fillMaxWidth().height(153.dp), 16f / 10f, HomePanel2)
      Box(Modifier.fillMaxWidth().height(153.dp).background(Brush.verticalGradient(listOf(Color.Transparent, HomePanel.copy(.2f), HomePanel))))
      Surface(Modifier.align(Alignment.TopEnd).padding(7.dp), color = Color.Black.copy(.68f), shape = RoundedCornerShape(8.dp)) {
        Text(if (video.width >= 3840 || video.height >= 2160) "4K" else video.resolution.ifBlank { "VIDEO" }, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
      }
      Column(Modifier.align(Alignment.BottomStart).padding(10.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(video.displayName, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Text(formatDuration(video.duration), color = TextMuted, style = MaterialTheme.typography.labelSmall)
          if (video.hasEmbeddedSubtitles) Icon(Icons.RoundedFilled.Subtitles, null, tint = NeonBlue, modifier = Modifier.size(15.dp))
        }
        if (video.duration > 0) {
          val pulse = rememberInfiniteTransition(label = "card-line").animateFloat(.7f, 1f, infiniteRepeatable(tween(1600), androidx.compose.animation.core.RepeatMode.Reverse), label = "line")
          Box(Modifier.fillMaxWidth(pulse.value).height(2.dp).clip(CircleShape).background(Brush.horizontalGradient(listOf(NeonPurple, NeonBlue))))
        }
      }
    }
  }
}

@Composable
private fun CompactVideoGrid(videos: List<Video>, repository: ThumbnailRepository, context: Context) {
  if (videos.isEmpty()) {
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = HomePanel) { Text("لا توجد ملفات في هذا الفلتر", color = TextMuted, modifier = Modifier.padding(20.dp)) }
    return
  }
  LazyVerticalGrid(columns = GridCells.Adaptive(150.dp), modifier = Modifier.height(490.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp), userScrollEnabled = false) {
    items(videos, key = { it.path }) { video ->
      Surface(onClick = { MediaUtils.playFile(video, context, "home_compact") }, shape = RoundedCornerShape(17.dp), color = HomePanel, border = BorderStroke(1.dp, Color.White.copy(.06f))) {
        Row(Modifier.height(78.dp), verticalAlignment = Alignment.CenterVertically) {
          ThumbnailImage(video, repository, Modifier.width(78.dp).fillMaxHeight(), 1f, HomePanel2)
          Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(video.displayName, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelMedium)
            Text("${formatDuration(video.duration)} • ${video.resolution.ifBlank { "فيديو" }}", color = TextMuted, style = MaterialTheme.typography.labelSmall)
          }
        }
      }
    }
  }
}

@Composable
private fun LibraryToolbar(filter: HomeFilter, sort: SortMode, mode: LibraryMode, resultCount: Int, onFilters: () -> Unit, onMode: () -> Unit) {
  Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(19.dp), color = HomePanel, border = BorderStroke(1.dp, Color.White.copy(.06f))) {
    Row(Modifier.padding(horizontal = 11.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
      Text("$resultCount ملف", color = TextMuted, style = MaterialTheme.typography.labelMedium)
      Spacer(Modifier.width(8.dp))
      HeroBadge(filter.title, NeonPurple)
      Spacer(Modifier.weight(1f))
      Text(sort.title, color = TextMuted, style = MaterialTheme.typography.labelSmall)
      Spacer(Modifier.width(7.dp))
      IconButton(onClick = onFilters) { Icon(Icons.RoundedFilled.Tune, null, tint = NeonPurple) }
      IconButton(onClick = onMode) { Icon(if (mode == LibraryMode.CARDS) Icons.RoundedFilled.ViewList else Icons.RoundedFilled.ViewQuilt, null, tint = NeonBlue) }
    }
  }
}

@Composable
private fun PlaylistItemRail(items: List<PlaylistItemEntity>, repository: ThumbnailRepository, context: Context) {
  if (items.isEmpty()) {
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = HomePanel) { Text("لم تتم إضافة ملفات محلية إلى المفضلة بعد", color = TextMuted, modifier = Modifier.padding(20.dp)) }
    return
  }
  LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 2.dp)) {
    items(items, key = { it.id }) { item ->
      val video = remember(item.filePath) { localVideoFromPath(item.filePath, item.fileName) }
      FavoriteCard(video, repository, context)
    }
  }
}

@Composable
private fun FavoriteCard(video: Video, repository: ThumbnailRepository, context: Context) {
  Surface(onClick = { MediaUtils.playFile(video.path, context, "home_favorite") }, modifier = Modifier.width(164.dp).height(205.dp), shape = RoundedCornerShape(21.dp), color = HomePanel, border = BorderStroke(1.dp, NeonPink.copy(.2f))) {
    Box {
      ThumbnailImage(video, repository, Modifier.fillMaxWidth().height(145.dp), 16f / 10f, HomePanel2)
      Box(Modifier.fillMaxWidth().height(145.dp).background(Brush.verticalGradient(listOf(Color.Transparent, HomePanel))))
      Icon(Icons.RoundedFilled.Favorite, null, tint = NeonPink, modifier = Modifier.align(Alignment.TopEnd).padding(9.dp).size(20.dp))
      Column(Modifier.align(Alignment.BottomStart).padding(10.dp).fillMaxWidth()) {
        Text(video.displayName, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text("مفضلة محلية", color = NeonPink, style = MaterialTheme.typography.labelSmall)
      }
    }
  }
}

@Composable
private fun PlaylistRail(playlists: List<PlaylistEntity>, onClick: (PlaylistEntity) -> Unit) {
  if (playlists.isEmpty()) {
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = HomePanel) { Text("أنشئ أول قائمة تشغيل من شاشة قوائم التشغيل.", color = TextMuted, modifier = Modifier.padding(20.dp)) }
    return
  }
  LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 2.dp)) {
    items(playlists, key = { it.id }) { playlist ->
      Surface(onClick = { onClick(playlist) }, modifier = Modifier.width(190.dp).height(120.dp), shape = RoundedCornerShape(22.dp), color = HomePanel, border = BorderStroke(1.dp, NeonPurple.copy(.22f))) {
        Box {
          Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(NeonPurple.copy(.24f), NeonPink.copy(.09f), HomePanel))))
          Column(Modifier.align(Alignment.Center).padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Icon(Icons.RoundedFilled.PlaylistPlay, null, tint = NeonPurple, modifier = Modifier.size(31.dp))
            Text(playlist.name, color = Color.White, fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
          }
        }
      }
    }
  }
}

@Composable
private fun LocalControlDeck(onSettings: () -> Unit) {
  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    HomeSectionTitle("🎛 غرفة التحكم", "الوصول السريع إلى خصائص التشغيل", Icons.RoundedFilled.Tune, NeonGreen)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(horizontal = 2.dp)) {
      item { ControlTile("4K / HDR", "الجودة", Icons.RoundedFilled.HdrOn, NeonOrange, onSettings) }
      item { ControlTile("CC / ASS", "الترجمة", Icons.RoundedFilled.Subtitles, NeonBlue, onSettings) }
      item { ControlTile("Audio", "الصوت", Icons.RoundedFilled.Equalizer, NeonPink, onSettings) }
      item { ControlTile("00:00", "المدة", Icons.RoundedFilled.AccessTime, NeonPurple, onSettings) }
      item { ControlTile("Crop", "التقسيم", Icons.RoundedFilled.AspectRatio, NeonGreen, onSettings) }
      item { ControlTile("Speed", "السرعة", Icons.RoundedFilled.Speed, NeonBlue, onSettings) }
      item { ControlTile("Decoder", "المحرك", Icons.RoundedFilled.Memory, NeonPurple, onSettings) }
    }
  }
}

@Composable
private fun ControlTile(value: String, title: String, icon: app.gyrolet.mpvrx.ui.icons.AppIcon, accent: Color, onClick: () -> Unit) {
  Surface(onClick = onClick, modifier = Modifier.width(116.dp).height(96.dp), shape = RoundedCornerShape(19.dp), color = HomePanel, border = BorderStroke(1.dp, accent.copy(.24f))) {
    Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Icon(icon, null, tint = accent, modifier = Modifier.size(20.dp))
      Text(value, color = Color.White, fontWeight = FontWeight.ExtraBold)
      Text(title, color = TextMuted, style = MaterialTheme.typography.labelSmall)
    }
  }
}

@Composable
private fun StorageAndLibraryCard(context: Context, data: HomeData) {
  val root = remember { Environment.getExternalStorageDirectory() }
  val total = remember { root.totalSpace }
  val free = remember { root.freeSpace }
  val used = (total - free).coerceAtLeast(0L)
  val fraction = if (total > 0) used.toFloat() / total.toFloat() else 0f
  val animated by animateFloatAsState(fraction, tween(900), label = "storage")
  Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(27.dp), color = HomePanel, border = BorderStroke(1.dp, NeonBlue.copy(.2f))) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(44.dp).clip(CircleShape).background(NeonBlue.copy(.13f)), contentAlignment = Alignment.Center) { Icon(Icons.RoundedFilled.SdCard, null, tint = NeonBlue) }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
          Text("نبض التخزين المحلي", color = Color.White, fontWeight = FontWeight.ExtraBold)
          Text("${formatBytes(data.videos.sumOf { it.size })} فيديو • ${data.folders.size} مجلد", color = TextMuted, style = MaterialTheme.typography.labelSmall)
        }
        Text("${(animated * 100).toInt()}%", color = NeonBlue, fontWeight = FontWeight.ExtraBold)
      }
      Box(Modifier.fillMaxWidth().height(10.dp).clip(CircleShape).background(HomePanel3)) {
        Box(Modifier.fillMaxWidth(animated).fillMaxHeight().clip(CircleShape).background(Brush.horizontalGradient(listOf(NeonBlue, NeonPurple, NeonPink))))
      }
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Stat("المستخدم", formatBytes(used), NeonBlue)
        Stat("المتاح", formatBytes(free), NeonGreen)
        Stat("الفيديو", "${data.videos.size}", NeonPurple)
        Stat("السجل", "${data.recent.size}", NeonOrange)
      }
    }
  }
}

@Composable
private fun Stat(title: String, value: String, accent: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(value, color = accent, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.labelLarge)
    Text(title, color = TextMuted, style = MaterialTheme.typography.labelSmall)
  }
}

@Composable
private fun FilterDialog(
  selectedFilter: HomeFilter,
  selectedSort: SortMode,
  libraryMode: LibraryMode,
  onFilter: (HomeFilter) -> Unit,
  onSort: (SortMode) -> Unit,
  onMode: (LibraryMode) -> Unit,
  onDismiss: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = HomePanel,
    title = { Text("المكتبة الذكية", color = Color.White, fontWeight = FontWeight.ExtraBold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("التصفية", color = NeonPurple, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
          items(HomeFilter.entries) { filter -> FilterChip(selected = filter == selectedFilter, onClick = { onFilter(filter) }, label = { Text(filter.title) }) }
        }
        Text("الترتيب", color = NeonBlue, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
          items(SortMode.entries) { sort -> FilterChip(selected = sort == selectedSort, onClick = { onSort(sort) }, label = { Text(sort.title) }) }
        }
        Text("طريقة العرض", color = NeonPink, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
          FilterChip(selected = libraryMode == LibraryMode.CARDS, onClick = { onMode(LibraryMode.CARDS) }, label = { Text("بطاقات") }, leadingIcon = { Icon(Icons.RoundedFilled.ViewQuilt, null) })
          FilterChip(selected = libraryMode == LibraryMode.COMPACT, onClick = { onMode(LibraryMode.COMPACT) }, label = { Text("مضغوط") }, leadingIcon = { Icon(Icons.RoundedFilled.ViewList, null) })
        }
      }
    },
    confirmButton = { TextButton(onClick = onDismiss) { Text("تم") } },
  )
}

@Composable
private fun LibraryStatsDialog(data: HomeData, onDismiss: () -> Unit) {
  val totalDuration = data.videos.sumOf { it.duration }
  val totalSize = data.videos.sumOf { it.size }
  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = HomePanel,
    title = { Text("إحصائيات مكتبتك", color = Color.White, fontWeight = FontWeight.ExtraBold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        BigStat("المجلدات", "${data.folders.size}", Icons.RoundedFilled.Folder, NeonBlue)
        BigStat("الفيديوهات المكتشفة", "${data.videos.size}", Icons.RoundedFilled.Movie, NeonPurple)
        BigStat("إجمالي الحجم", formatBytes(totalSize), Icons.RoundedFilled.SdCard, NeonOrange)
        BigStat("إجمالي المدة", formatDuration(totalDuration), Icons.RoundedFilled.AccessTime, NeonGreen)
        BigStat("المفضلة", "${data.favorites.size}", Icons.RoundedFilled.Favorite, NeonPink)
        HorizontalDivider(color = Color.White.copy(.08f))
        Text("هذه الأرقام مبنية على الملفات المحلية التي اكتشفها المشغل، وليست بيانات خدمة خارجية.", color = TextMuted, style = MaterialTheme.typography.bodySmall)
      }
    },
    confirmButton = { TextButton(onClick = onDismiss) { Text("إغلاق") } },
  )
}

@Composable
private fun BigStat(title: String, value: String, icon: app.gyrolet.mpvrx.ui.icons.AppIcon, accent: Color) {
  Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    Icon(icon, null, tint = accent, modifier = Modifier.size(24.dp))
    Spacer(Modifier.width(10.dp))
    Text(title, color = TextMuted, modifier = Modifier.weight(1f))
    Text(value, color = Color.White, fontWeight = FontWeight.ExtraBold)
  }
}

@Composable
private fun ThumbnailImage(video: Video, repository: ThumbnailRepository, modifier: Modifier, aspectRatio: Float, fallback: Color) {
  var bitmap by remember(video.path, video.size, video.dateModified) { mutableStateOf<android.graphics.Bitmap?>(null) }
  LaunchedEffect(video.path, video.size, video.dateModified) {
    bitmap = repository.getThumbnail(video, 640, (640 / aspectRatio).toInt().coerceAtLeast(1))
  }
  Box(modifier = modifier.background(fallback), contentAlignment = Alignment.Center) {
    bitmap?.let {
      Image(bitmap = it.asImageBitmap(), contentDescription = video.displayName, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
    } ?: Icon(Icons.RoundedFilled.Movie, null, tint = TextMuted.copy(.35f), modifier = Modifier.size(42.dp))
  }
}

private fun localVideoFromPath(path: String, title: String): Video {
  val file = File(path)
  val extension = file.extension.lowercase(Locale.ROOT)
  val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "video/*"
  val display = title.ifBlank { file.name }
  return Video(
    id = path.hashCode().toLong(),
    title = FileTypeUtils.stripExtension(display),
    displayName = display,
    path = path,
    uri = Uri.fromFile(file),
    duration = 0L,
    durationFormatted = "",
    size = file.length(),
    sizeFormatted = formatBytes(file.length()),
    dateModified = file.lastModified(),
    dateAdded = file.lastModified(),
    mimeType = mime,
    bucketId = file.parent.orEmpty(),
    bucketDisplayName = file.parentFile?.name.orEmpty(),
    width = 0,
    height = 0,
    fps = 0f,
    resolution = "",
    hasEmbeddedSubtitles = false,
    videoCodec = "",
    videoCodecMimeType = mime,
    isAudio = false,
  )
}

private fun formatDuration(ms: Long): String {
  if (ms <= 0) return "—"
  val total = ms / 1000
  val h = total / 3600
  val m = (total % 3600) / 60
  return if (h > 0) "%d:%02d:%02d".format(h, m, total % 60) else "%d:%02d".format(m, total % 60)
}

private fun formatSeconds(seconds: Int): String = formatDuration(seconds.coerceAtLeast(0).toLong() * 1000L)

private fun formatBytes(bytes: Long): String {
  if (bytes <= 0) return "0 B"
  val units = arrayOf("B", "KB", "MB", "GB", "TB")
  var value = bytes.toDouble()
  var index = 0
  while (value >= 1024 && index < units.lastIndex) { value /= 1024; index++ }
  return if (value >= 100 || index == 0) "%.0f %s".format(value, units[index]) else "%.1f %s".format(value, units[index])
}
