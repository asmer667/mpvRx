package app.gyrolet.mpvrx.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.components.VideoItem

@Composable
fun FolderDetailScreen(
    folderName: String,
    videosInFolder: List<VideoItem>,
    getThumbnail: (VideoItem) -> Bitmap?,
    onVideoClick: (VideoItem) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize().background(Color(0xFF030108))) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // الشريط العلوي مع زر الرجوع
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(38.dp).clip(CircleShape).background(Color(0x221A103C))
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع", tint = Color.White)
                    }
                    Column {
                        Text(folderName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("${videosInFolder.size} ملفات فيديو", color = Color(0xCCFFFFFF), fontSize = 11.sp)
                    }
                }
                
                IconButton(onClick = { }, modifier = Modifier.size(38.dp).clip(CircleShape).background(Color(0x221A103C))) {
                    Icon(Icons.Default.Search, contentDescription = "بحث", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // شبكة الحلقات/الفيديوهات بالتأثير الزجاجي المضيء
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(videosInFolder) { video ->
                    EpisodeCyberCard(
                        video = video,
                        thumbnail = getThumbnail(video),
                        onPlayClick = { onVideoClick(video) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EpisodeCyberCard(
    video: VideoItem,
    thumbnail: Bitmap?,
    onPlayClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, Brush.verticalGradient(listOf(Color(0xFF00F0FF), Color(0xFFFF007A))), RoundedCornerShape(18.dp))
            .background(Color(0x1A14082D))
            .clickable(onClick = onPlayClick)
    ) {
        // الصورة المصغرة للفيديو
        thumbnail?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(125.dp)
            )
        } ?: Box(modifier = Modifier.fillMaxWidth().height(125.dp).background(Color.Black))

        // زر التفضيل العلوي
        IconButton(
            onClick = { isFavorite = !isFavorite },
            modifier = Modifier.align(Alignment.TopEnd).padding(6.dp).size(28.dp).clip(CircleShape).background(Color.Black.copy(0.5f))
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = if (isFavorite) Color(0xFFFF007A) else Color.White,
                modifier = Modifier.size(16.dp)
            )
        }

        // تفاصيل الحلقة/الفيديو في الأسفل
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(Color.Black.copy(0.85f))
                .padding(8.dp)
        ) {
            Text(video.title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(video.durationFormatted, color = Color(0xCCFFFFFF), fontSize = 9.sp)
                Icon(Icons.Default.PlayCircleFilled, contentDescription = null, tint = Color(0xFF00F0FF), modifier = Modifier.size(18.dp))
            }
        }
    }
}
