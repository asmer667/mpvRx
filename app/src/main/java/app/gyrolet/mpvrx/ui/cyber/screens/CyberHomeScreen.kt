/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - Home Screen
 * الشاشة الرئيسية الكاملة بكل المكونات
 */

package app.gyrolet.mpvrx.ui.cyber.screens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.cyber.components.CyberDockBottomBar
import app.gyrolet.mpvrx.ui.cyber.components.CyberDockItem
import app.gyrolet.mpvrx.ui.cyber.components.CyberFolderCard
import app.gyrolet.mpvrx.ui.cyber.components.CyberGlowBackground
import app.gyrolet.mpvrx.ui.cyber.components.CyberHeroItem
import app.gyrolet.mpvrx.ui.cyber.components.CyberHeroStack
import app.gyrolet.mpvrx.ui.cyber.components.CyberSidebar
import app.gyrolet.mpvrx.ui.cyber.components.CyberSidebarItem
import app.gyrolet.mpvrx.ui.cyber.components.CyberStatsWidget
import app.gyrolet.mpvrx.ui.cyber.components.CyberTopAppBar
import app.gyrolet.mpvrx.ui.cyber.components.CyberVideoCard
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors
import app.gyrolet.mpvrx.ui.icons.Icons

/**
 * بيانات الفيديو (مؤقتة - سيتم استبدالها بالبيانات الحقيقية)
 */
data class CyberVideoItem(
    val id: String,
    val title: String,
    val duration: String? = null,
    val resolution: String? = null,
    val thumbnail: Bitmap? = null,
)

/**
 * بيانات المجلد
 */
data class CyberFolderItem(
    val id: String,
    val name: String,
    val count: Int,
)

/**
 * الشاشة الرئيسية الكاملة بأسلوب Cyber
 *
 * @param recentVideos آخر الفيديوهات (تُعرض في HeroStack والقائمة السفلية)
 * @param folders المجلدات
 * @param onVideoClick عند الضغط على فيديو
 * @param onFolderClick عند الضغط على مجلد
 * @param onSettingsClick عند الضغط على الإعدادات
 * @param onPlayClick عند الضغط على زر التشغيل في HeroStack
 */
@Composable
fun CyberHomeScreen(
    modifier: Modifier = Modifier,
    recentVideos: List<CyberVideoItem> = emptyList(),
    folders: List<CyberFolderItem> = emptyList(),
    onVideoClick: (String) -> Unit = {},
    onFolderClick: (String) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPlayClick: (String) -> Unit = {},
) {
    // Sidebar items
    val sidebarItems = remember {
        listOf(
            CyberSidebarItem("home", "الرئيسية", Icons.RoundedFilled.Home),
            CyberSidebarItem("library", "مكتبة الفيديو", Icons.RoundedFilled.VideoLibrary),
            CyberSidebarItem("folders", "المجلدات", Icons.RoundedFilled.Folder),
            CyberSidebarItem("playlists", "قوائم التشغيل", Icons.RoundedFilled.PlaylistPlay),
            CyberSidebarItem("favorites", "المفضلة", Icons.RoundedFilled.Favorite),
            CyberSidebarItem("history", "المشاهدة لاحقاً", Icons.RoundedFilled.History),
            CyberSidebarItem("settings", "الإعدادات", Icons.RoundedFilled.Settings),
        )
    }

    // Dock items
    val dockItems = remember {
        listOf(
            CyberDockItem("playlist", "قائمة", Icons.RoundedFilled.PlaylistPlay),
            CyberDockItem("favorites", "المفضلة", Icons.RoundedFilled.Favorite),
            CyberDockItem("home", "الرئيسية", Icons.RoundedFilled.Home),
            CyberDockItem("library", "المكتبة", Icons.RoundedFilled.VideoLibrary),
            CyberDockItem("settings", "الإعدادات", Icons.RoundedFilled.Settings),
        )
    }

    // بيانات افتراضية للعرض إذا كانت القائمة فارغة
    val displayVideos = remember(recentVideos) {
        if (recentVideos.isEmpty()) {
            listOf(
                CyberVideoItem("1", "Interstellar", "2:49:00", "4K"),
                CyberVideoItem("2", "The Batman", "2:57:00", "4K"),
                CyberVideoItem("3", "Dune", "2:35:12", "4K"),
                CyberVideoItem("4", "Inception", "2:28:16", "4K"),
                CyberVideoItem("5", "The Witcher", "1:22:00", "4K"),
            )
        } else {
            recentVideos
        }
    }

    val displayFolders = remember(folders) {
        if (folders.isEmpty()) {
            listOf(
                CyberFolderItem("1", "أفلام", 47),
                CyberFolderItem("2", "مسلسلات", 32),
                CyberFolderItem("3", "أنمي", 16),
                CyberFolderItem("4", "وثائقيات", 12),
            )
        } else {
            folders
        }
    }

    // تحويل فيديوهات Hero
    val heroItems = displayVideos.take(5).map { video ->
        CyberHeroItem(
            id = video.id,
            title = video.title,
            subtitle = "فيديو مميز",
            duration = video.duration,
            badge = video.resolution,
            thumbnail = video.thumbnail,
        )
    }

    var activeSidebarId by remember { mutableStateOf("home") }
    var activeDockId by remember { mutableStateOf("home") }

    CyberGlowBackground(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            // الشريط الجانبي
            CyberSidebar(
                items = sidebarItems,
                activeId = activeSidebarId,
                onItemClick = { activeSidebarId = it },
                modifier = Modifier
                    .width(220.dp)
                    .fillMaxHeight(),
            )

            // المحتوى الرئيسي
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                // الشريط العلوي
                CyberTopAppBar(
                    title = "مشغل الوسائط",
                    onSettingsClick = onSettingsClick,
                )

                Spacer(modifier = Modifier.height(12.dp))

                // المحتوى - قابل للتمرير
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Hero Stack (البطاقة الرئيسية + البطاقات المائلة)
                    item {
                        if (heroItems.isNotEmpty()) {
                            CyberHeroStack(
                                items = heroItems,
                                onPlayClick = onPlayClick,
                                onItemClick = onVideoClick,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    // Stats Widget
                    item {
                        CyberStatsWidget(
                            title = "التخزين المحلي",
                            usedText = "412.6 GB",
                            totalText = "604 GB",
                            percentage = 0.68f,
                        )
                    }

                    // عنوان المجلدات
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "المجلدات",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "عرض الكل",
                                color = Cyber3DColors.CyanNeon,
                                fontSize = 11.sp,
                            )
                        }
                    }

                    // المجلدات
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(displayFolders) { folder ->
                                CyberFolderCard(
                                    folderName = folder.name,
                                    itemCount = folder.count,
                                    onClick = { onFolderClick(folder.id) },
                                )
                            }
                        }
                    }

                    // عنوان الأحدث
                    item {
                        Text(
                            text = "🔥 الأحدث في المكتبة",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    // قائمة الفيديوهات
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(vertical = 8.dp),
                        ) {
                            items(displayVideos) { video ->
                                CyberVideoCard(
                                    title = video.title,
                                    thumbnail = video.thumbnail,
                                    duration = video.duration,
                                    resolution = video.resolution,
                                    onClick = { onVideoClick(video.id) },
                                )
                            }
                        }
                    }

                    // مساحة إضافية أسفل
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }

                // الشريط السفلي
                CyberDockBottomBar(
                    items = dockItems,
                    activeId = activeDockId,
                    onItemClick = { activeDockId = it },
                )
            }
        }
    }
}
