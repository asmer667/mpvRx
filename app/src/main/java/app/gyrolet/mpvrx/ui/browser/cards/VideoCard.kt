package app.gyrolet.mpvrx.ui.browser.cards // عدل اسم الـ package ليطابق مشروعك إذا كان مختلفاً

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.video.model.VideoItem
import com.app.video.ui.theme.Cyber3DColors

@Composable
fun VideoCard(
    video: VideoItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    thumbnailBitmap: Bitmap? = null,
    isSelected: Boolean = false,
    isGridMode: Boolean = true,
    showThumbnails: Boolean = true,
    progressPercentage: Float? = null,
    onMoreClick: (() -> Unit)? = null
) {
    Cyber3DCard(
        onClick = onClick,
        isSelected = isSelected,
        modifier = modifier.fillMaxWidth()
    ) {
        if (isGridMode) {
            // تصميم الشبكة 3D Grid Layout
            Column(modifier = Modifier.fillMaxWidth()) {
                Thumbnail3DBox(
                    thumbnailBitmap = thumbnailBitmap,
                    showThumbnails = showThumbnails,
                    duration = video.durationFormatted,
                    resolution = video.resolution,
                    progressPercentage = progressPercentage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = video.sizeFormatted,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    
                    if (onMoreClick != null) {
                        IconButton(onClick = onMoreClick) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        } else {
            // تصميم القائمة 3D List Layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Thumbnail3DBox(
                    thumbnailBitmap = thumbnailBitmap,
                    showThumbnails = showThumbnails,
                    duration = video.durationFormatted,
                    resolution = null,
                    progressPercentage = progressPercentage,
                    modifier = Modifier
                        .width(120.dp)
                        .height(70.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (video.resolution.isNotEmpty()) {
                            CyberHudBadge(
                                text = video.resolution,
                                accentColor = Cyber3DColors.CyanNeon
                            )
                        }
                        Text(
                            text = video.sizeFormatted,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }

                if (onMoreClick != null) {
                    IconButton(onClick = onMoreClick) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Thumbnail3DBox(
    thumbnailBitmap: Bitmap?,
    showThumbnails: Boolean,
    duration: String?,
    resolution: String?,
    progressPercentage: Float?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
    ) {
        // 1. إضاءة التوهج خلف الصورة المصغرة (3D Ambient Glow)
        if (showThumbnails && thumbnailBitmap != null) {
            Image(
                bitmap = thumbnailBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .blur(20.dp),
                contentScale = ContentScale.Crop,
                alpha = 0.5f
            )
            
            // 2. الصورة الرئيسية
            Image(
                bitmap = thumbnailBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // أيقونة افتراضية مستقبلية في حال عدم وجود صورة مصغرة
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Cyber3DColors.PurpleNeon.copy(alpha = 0.4f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Cyber3DColors.CyanNeon,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // 3. شارات الـ HUD على الصورة المصغرة
        duration?.let {
            CyberHudBadge(
                text = it,
                accentColor = Color.White,
                backgroundColor = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
            )
        }

        resolution?.let {
            CyberHudBadge(
                text = it,
                accentColor = Cyber3DColors.CyanNeon,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
            )
        }

        // 4. شريط التقدم النيون المضيء 3D
        if (progressPercentage != null && progressPercentage > 0f) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progressPercentage)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Cyber3DColors.CyanNeon, Cyber3DColors.MagentaNeon)
                            )
                        )
                )
            }
        }
    }
}
