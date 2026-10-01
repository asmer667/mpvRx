package app.gyrolet.mpvrx.ui.browser.folderlist // عدل اسم الـ package ليطابق مشروعك إذا كان مختلفاً

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.video.model.VideoItem
import com.app.video.ui.components.*
import com.app.video.ui.theme.Cyber3DColors

@Composable
fun VideoListScreen(
    videos: List<VideoItem>,
    isLoading: Boolean,
    getThumbnail: (VideoItem) -> Bitmap?,
    onVideoClick: (VideoItem) -> Unit,
    onMoreClick: (VideoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var isGridMode by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(CyberTab.HOME) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Cyber3DColors.DarkBackground,
        topBar = {
            CyberTopAppBar(
                title = "Cyber Player",
                isGridMode = isGridMode,
                onToggleViewMode = { isGridMode = !isGridMode },
                onSearchClick = { /* تنفيذ البحث */ }
            )
        },
        bottomBar = {
            CyberDockBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CyberLoadingGrid(isGrid = isGridMode)
            } else {
                if (isGridMode) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(videos, key = { it.id }) { video ->
                            VideoCard(
                                video = video,
                                onClick = { onVideoClick(video) },
                                thumbnailBitmap = getThumbnail(video),
                                isGridMode = true,
                                onMoreClick = { onMoreClick(video) }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(videos, key = { it.id }) { video ->
                            VideoCard(
                                video = video,
                                onClick = { onVideoClick(video) },
                                thumbnailBitmap = getThumbnail(video),
                                isGridMode = false,
                                onMoreClick = { onMoreClick(video) }
                            )
                        }
                    }
                }
            }
        }
    }
}
