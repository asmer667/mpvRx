package app.gyrolet.mpvrx.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Environment
import android.os.StatFs
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import app.gyrolet.mpvrx.ui.components.VideoItem
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons
import java.util.Locale

fun getRealSystemStorage(): StorageInfo {
    return try {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val totalBytes = stat.blockCountLong * stat.blockSizeLong
        val availableBytes = stat.availableBlocksLong * stat.blockSizeLong
        val usedBytes = totalBytes - availableBytes
        StorageInfo(usedBytes = usedBytes, totalBytes = totalBytes)
    } catch (e: Exception) {
        StorageInfo()
    }
}

data class StorageInfo(
    val usedBytes: Long = 443048781414L,
    val totalBytes: Long = 657158307840L
) {
    val usedGB: String get() = String.format(Locale.US, "%.1f GB", usedBytes / (1024.0 * 1024.0 * 1024.0))
    val totalGB: String get() = String.format(Locale.US, "%.0f GB", totalBytes / (1024.0 * 1024.0 * 1024.0))
    val percentage: Float get() = if (totalBytes > 0) (usedBytes.toFloat() / totalBytes.toFloat()) else 0f
}

data class FolderInfo(
    val name: String,
    val count: Int
)

private object CyberTheme100 {
    val BackgroundDark = Color(0xFF030108)
    val GlassSurface = Color(0x1A14082D)
    val GlassBorderGlow = Color(0x99B300FF)
    val CyanNeon = Color(0xFF00F0FF)
    val MagentaNeon = Color(0xFFFF007A)
    val ElectricPurple = Color(0xFF8A00FF)
    val TextSecondary = Color(0xCCFFFFFF)
}

fun Modifier.realGlassBlur(
    blurRadius: Float = 30f,
    shape: Shape = RoundedCornerShape(18.dp)
) = this
    .clip(shape)
    .graphicsLayer {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            renderEffect = RenderEffect
                .createBlurEffect(blurRadius, blurRadius, Shader.TileMode.CLAMP)
                .asComposeRenderEffect()
        }
    }

fun Modifier.cyberNeonShadow(
    color: Color,
    blurRadius: Dp = 16.dp,
    cornerRadius: Dp = 18.dp
) = this.drawBehind {
    drawIntoCanvas { canvas ->
        val paint = NativePaint().apply {
            this.color = color.toArgb()
            this.isAntiAlias = true
            this.maskFilter = android.graphics.BlurMaskFilter(
                blurRadius.toPx(),
                android.graphics.BlurMaskFilter.Blur.NORMAL
            )
        }
        canvas.nativeCanvas.drawRoundRect(
            0f, 0f, size.width, size.height,
            cornerRadius.toPx(), cornerRadius.toPx(),
            paint
        )
    }
}

@Composable
fun DashboardScreen(
    featuredVideo: VideoItem?,
    recentVideos: List<VideoItem>,
    getThumbnail: (VideoItem) -> Bitmap?,
    onVideoClick: (VideoItem) -> Unit,
    foldersList: List<FolderInfo> = listOf(
        FolderInfo("أفلام", 47),
        FolderInfo("مسلسلات", 32),
        FolderInfo("أنمي", 16),
        FolderInfo("وثائقيات", 12)
    ),
    resumeProgress: Float = 0.45f,
    onSearchClick: () -> Unit = {},
    onThemeToggleClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onFolderClick: (FolderInfo) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeTab by remember { mutableStateOf("home") }
    var selectedCategory by remember { mutableStateOf("الكل") }

    val liveStorageInfo = remember { getRealSystemStorage() }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_VIDEO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var isPermissionGranted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, permissionToRequest) == PackageManager.PERMISSION_GRANTED)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted -> isPermissionGranted = isGranted }

    LaunchedEffect(Unit) {
        if (!isPermissionGranted) permissionLauncher.launch(permissionToRequest)
    }

    Box(modifier = modifier.fillMaxSize().background(CyberTheme100.BackgroundDark)) {
        Box(
            modifier = Modifier
                .size(500.dp)
                .align(Alignment.TopStart)
                .blur(160.dp)
                .background(CyberTheme100.ElectricPurple.copy(alpha = 0.35f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(450.dp)
                .align(Alignment.BottomEnd)
                .blur(150.dp)
                .background(CyberTheme100.CyanNeon.copy(alpha = 0.22f), CircleShape)
        )

        Row(modifier = Modifier.fillMaxSize()) {
            CyberSidebar100(
                activeTab = activeTab,
                onTabSelect = { activeTab = it },
                storageInfo = liveStorageInfo,
                onSettingsClick = onSettingsClick,
                modifier = Modifier.width(235.dp).fillMaxHeight()
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                CyberTopBar100(
                    onSearchClick = onSearchClick,
                    onThemeToggleClick = onThemeToggleClick,
                    onSettingsClick = onSettingsClick
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(modifier = Modifier.weight(0.72f), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                FeaturedHeroCard100(
                                    video = featuredVideo,
                                    thumbnail = featuredVideo?.let { getThumbnail(it) },
                                    onPlayClick = { featuredVideo?.let(onVideoClick) }
                                )

                                SubCategoryDock100(
                                    selectedCategory = selectedCategory,
                                    onCategorySelect = { cat ->
                                        selectedCategory = cat
                                        onCategoryClick(cat)
                                    }
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("مكتبة الفيديو المحلية", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        "الكل (${foldersList.sumOf { it.count }})",
                                        color = CyberTheme100.CyanNeon,
                                        fontSize = 11.sp,
                                        modifier = Modifier.clickable { onCategoryClick("الكل") }
                                    )
                                }

                                FolderCardsRow100(folders = foldersList, onFolderClick = onFolderClick)
                            }

                            Column(modifier = Modifier.weight(0.28f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                ResumePlaybackWidget100(
                                    video = featuredVideo,
                                    progress = resumeProgress,
                                    onClick = { featuredVideo?.let(onVideoClick) }
                                )
                                RecentFilesWidget100(
                                    videos = recentVideos,
                                    getThumbnail = getThumbnail,
                                    onVideoClick = onVideoClick,
                                    modifier = Modifier.height(370.dp)
                                )
                            }
                        }
                    }

                    item {
                        Text("الأحدث في المكتبة 🔥", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }

                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(18.dp), contentPadding = PaddingValues(vertical = 10.dp)) {
                            items(recentVideos) { video ->
                                Realistic3DCardStack(
                                    video = video,
                                    thumbnail = getThumbnail(video),
                                    onClick = { onVideoClick(video) }
                                )
                            }
                        }
                    }
                }

                CyberBottomDock100(
                    activeTab = activeTab,
                    onTabSelect = { activeTab = it },
                    onSettingsClick = onSettingsClick
                )
            }
        }
    }
}

@Composable
private fun Realistic3DCardStack(
    video: VideoItem,
    thumbnail: Bitmap?,
    onClick: () -> Unit
) {
    val density = LocalDensity.current.density
    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isHovered) 1.05f else 1f, animationSpec = tween(200), label = "scale")

    Box(
        modifier = Modifier
            .width(130.dp)
            .height(180.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                cameraDistance = 16f * density
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = 12f
                    translationY = 12f
                    rotationZ = -5f
                }
                .clip(RoundedCornerShape(18.dp))
                .background(CyberTheme100.MagentaNeon.copy(alpha = 0.28f))
                .border(1.dp, CyberTheme100.MagentaNeon.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = 6f
                    translationY = 6f
                    rotationZ = -2.5f
                }
                .clip(RoundedCornerShape(18.dp))
                .background(CyberTheme100.ElectricPurple.copy(alpha = 0.35f))
                .border(1.dp, CyberTheme100.CyanNeon.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .cyberNeonShadow(CyberTheme100.CyanNeon.copy(alpha = 0.35f), blurRadius = 10.dp)
                .realGlassBlur(blurRadius = 20f, shape = RoundedCornerShape(18.dp))
                .border(
                    1.5.dp,
                    Brush.linearGradient(
                        colors = listOf(CyberTheme100.CyanNeon, CyberTheme100.MagentaNeon, Color.Transparent)
                    ),
                    RoundedCornerShape(18.dp)
                )
                .background(CyberTheme100.GlassSurface)
        ) {
            thumbnail?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.25f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.92f)
                            )
                        )
                    )
            )
            Text(
                text = video.title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            )
        }
    }
}

@Composable
private fun CyberSidebar100(
    activeTab: String,
    onTabSelect: (String) -> Unit,
    storageInfo: StorageInfo,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .realGlassBlur(blurRadius = 35f, shape = RoundedCornerShape(26.dp))
            .border(1.dp, CyberTheme100.GlassBorderGlow, RoundedCornerShape(26.dp))
            .background(CyberTheme100.GlassSurface)
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 14.dp)
                ) {
                    Icon(
                        imageVector = Icons.RoundedFilled.PlayCircle,
                        contentDescription = null,
                        tint = CyberTheme100.CyanNeon,
                        modifier = Modifier.size(30.dp)
                    )
                    Text("mpvRx", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                }

                SidebarItem100("الرئيسية", Icons.RoundedFilled.Home, selected = activeTab == "home") { onTabSelect("home") }
                SidebarItem100("مكتبة الفيديو", Icons.RoundedFilled.VideoLibrary, selected = activeTab == "library") { onTabSelect("library") }
                SidebarItem100("المجلدات", Icons.RoundedFilled.Folder, selected = activeTab == "folders") { onTabSelect("folders") }
                SidebarItem100("قوائم التشغيل", Icons.RoundedFilled.PlaylistPlay, selected = activeTab == "playlists") { onTabSelect("playlists") }
                SidebarItem100("المفضلة", Icons.RoundedFilled.FavoriteBorder, selected = activeTab == "favorites") { onTabSelect("favorites") }
                SidebarItem100("الإعدادات", Icons.RoundedFilled.Settings, selected = activeTab == "settings") {
                    onTabSelect("settings")
                    onSettingsClick()
                }
            }

            StorageGaugeWidget100(storageInfo = storageInfo)
        }
    }
}

@Composable
private fun SidebarItem100(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(if (selected) CyberTheme100.CyanNeon.copy(0.2f) else Color.Transparent, label = "bg")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .background(bgColor)
            .border(
                if (selected) 1.dp else 0.dp,
                if (selected) CyberTheme100.CyanNeon.copy(0.6f) else Color.Transparent,
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) CyberTheme100.CyanNeon else CyberTheme100.TextSecondary,
            modifier = Modifier.size(19.dp)
        )
        Text(
            title,
            color = if (selected) Color.White else CyberTheme100.TextSecondary,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun StorageGaugeWidget100(storageInfo: StorageInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.Black.copy(0.5f))
            .border(1.dp, Color.White.copy(0.12f), RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(44.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(Color.White.copy(0.12f), 0f, 360f, false, style = Stroke(4.5.dp.toPx()))
                    drawArc(
                        brush = Brush.sweepGradient(listOf(CyberTheme100.CyanNeon, CyberTheme100.MagentaNeon, CyberTheme100.CyanNeon)),
                        startAngle = -90f,
                        sweepAngle = storageInfo.percentage * 360f,
                        useCenter = false,
                        style = Stroke(4.5.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Text("${(storageInfo.percentage * 100).toInt()}%", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
            }
            Column {
                Text("التخزين المحلي", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("${storageInfo.usedGB} / ${storageInfo.totalGB}", color = CyberTheme100.TextSecondary, fontSize = 9.sp)
            }
        }
    }
}

@Composable
private fun CyberTopBar100(
    onSearchClick: () -> Unit,
    onThemeToggleClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("المشغل المحلي العالي الأداء", color = CyberTheme100.TextSecondary, fontSize = 11.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.size(34.dp).clip(CircleShape).background(CyberTheme100.GlassSurface)
            ) {
                Icon(
                    imageVector = Icons.RoundedFilled.Search,
                    contentDescription = "بحث",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            IconButton(
                onClick = onThemeToggleClick,
                modifier = Modifier.size(34.dp).clip(CircleShape).background(CyberTheme100.GlassSurface)
            ) {
                Icon(
                    imageVector = Icons.RoundedFilled.Brightness6,
                    contentDescription = "الثيم",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.size(34.dp).clip(CircleShape).background(CyberTheme100.GlassSurface)
            ) {
                Icon(
                    imageVector = Icons.RoundedFilled.Settings,
                    contentDescription = "الإعدادات",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun FeaturedHeroCard100(video: VideoItem?, thumbnail: Bitmap?, onPlayClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .cyberNeonShadow(CyberTheme100.ElectricPurple.copy(alpha = 0.5f), blurRadius = 18.dp)
            .realGlassBlur(blurRadius = 25f, shape = RoundedCornerShape(24.dp))
            .border(1.5.dp, Brush.horizontalGradient(listOf(CyberTheme100.CyanNeon, CyberTheme100.MagentaNeon)), RoundedCornerShape(24.dp))
            .background(CyberTheme100.GlassSurface)
    ) {
        thumbnail?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(Color.Black.copy(0.88f), Color.Transparent)))
        )
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(18.dp)) {
            Text(video?.title ?: "The Last of Us", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onPlayClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.background(
                    Brush.horizontalGradient(listOf(CyberTheme100.CyanNeon, CyberTheme100.ElectricPurple)),
                    RoundedCornerShape(14.dp)
                )
            ) {
                Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.RoundedFilled.PlayArrow,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text("تشغيل الآن", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun SubCategoryDock100(selectedCategory: String, onCategorySelect: (String) -> Unit) {
    val items = listOf("الكل", "موسيقى", "برامج", "وثائقيات", "أنمي", "مسلسلات", "أفلام")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .realGlassBlur(blurRadius = 20f, shape = RoundedCornerShape(20.dp))
            .border(1.dp, CyberTheme100.GlassBorderGlow, RoundedCornerShape(20.dp))
            .background(CyberTheme100.GlassSurface)
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEach { title ->
            val isSelected = title == selectedCategory
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) CyberTheme100.CyanNeon.copy(0.25f) else Color.Transparent)
                    .clickable { onCategorySelect(title) }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    title,
                    color = if (isSelected) CyberTheme100.CyanNeon else Color.White.copy(0.85f),
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun FolderCardsRow100(folders: List<FolderInfo>, onFolderClick: (FolderInfo) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(folders) { folder ->
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color.White.copy(0.15f), RoundedCornerShape(16.dp))
                    .background(CyberTheme100.GlassSurface)
                    .clickable { onFolderClick(folder) }
                    .padding(12.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                    Icon(
                        imageVector = Icons.RoundedFilled.Folder,
                        contentDescription = null,
                        tint = CyberTheme100.CyanNeon,
                        modifier = Modifier.size(22.dp)
                    )
                    Column {
                        Text(folder.name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("${folder.count} عنصر", color = CyberTheme100.TextSecondary, fontSize = 8.5.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResumePlaybackWidget100(video: VideoItem?, progress: Float, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, CyberTheme100.CyanNeon.copy(0.5f), RoundedCornerShape(18.dp))
            .background(CyberTheme100.GlassSurface)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("استئناف التشغيل", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Icon(
                    imageVector = Icons.RoundedFilled.PlayCircle,
                    contentDescription = null,
                    tint = CyberTheme100.CyanNeon,
                    modifier = Modifier.size(17.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                color = CyberTheme100.CyanNeon,
                trackColor = Color.White.copy(0.12f)
            )
        }
    }
}

@Composable
private fun RecentFilesWidget100(
    videos: List<VideoItem>,
    getThumbnail: (VideoItem) -> Bitmap?,
    onVideoClick: (VideoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color.White.copy(0.12f), RoundedCornerShape(20.dp))
            .background(CyberTheme100.GlassSurface)
            .padding(12.dp)
    ) {
        Column {
            Text("الملفات الأخيرة", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(videos) { video ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onVideoClick(video) }
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black)
                        ) {
                            getThumbnail(video)?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                video.title,
                                color = Color.White,
                                fontSize = 10.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(video.durationFormatted, color = CyberTheme100.TextSecondary, fontSize = 8.5.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CyberBottomDock100(
    activeTab: String,
    onTabSelect: (String) -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .cyberNeonShadow(CyberTheme100.MagentaNeon.copy(alpha = 0.4f), blurRadius = 12.dp, cornerRadius = 26.dp)
            .realGlassBlur(blurRadius = 30f, shape = RoundedCornerShape(26.dp))
            .border(1.dp, Brush.horizontalGradient(listOf(CyberTheme100.CyanNeon, CyberTheme100.MagentaNeon)), RoundedCornerShape(26.dp))
            .background(CyberTheme100.GlassSurface),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onTabSelect("home") }) {
                Icon(
                    imageVector = Icons.RoundedFilled.Home,
                    contentDescription = "الرئيسية",
                    tint = if (activeTab == "home") CyberTheme100.CyanNeon else Color.White.copy(0.6f)
                )
            }
            IconButton(onClick = { onTabSelect("library") }) {
                Icon(
                    imageVector = Icons.RoundedFilled.VideoLibrary,
                    contentDescription = "المكتبة",
                    tint = if (activeTab == "library") CyberTheme100.CyanNeon else Color.White.copy(0.6f)
                )
            }
            IconButton(onClick = { onTabSelect("favorites") }) {
                Icon(
                    imageVector = Icons.RoundedFilled.FavoriteBorder,
                    contentDescription = "المفضلة",
                    tint = if (activeTab == "favorites") CyberTheme100.CyanNeon else Color.White.copy(0.6f)
                )
            }
            IconButton(onClick = { onTabSelect("music") }) {
                Icon(
                    imageVector = Icons.RoundedFilled.Audiotrack,
                    contentDescription = "موسيقى",
                    tint = if (activeTab == "music") CyberTheme100.CyanNeon else Color.White.copy(0.6f)
                )
            }
            IconButton(onClick = {
                onTabSelect("settings")
                onSettingsClick()
            }) {
                Icon(
                    imageVector = Icons.RoundedFilled.Settings,
                    contentDescription = "الإعدادات",
                    tint = if (activeTab == "settings") CyberTheme100.CyanNeon else Color.White.copy(0.6f)
                )
            }
        }
    }
}