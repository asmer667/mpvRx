package app.gyrolet.mpvrx.ui.components // عدل اسم الـ package ليطابق مشروعك إذا كان مختلفاً

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.video.ui.theme.Cyber3DColors

@Composable
fun CyberPlayerOverlay(
    isPlaying: Boolean,
    title: String,
    currentTimeFormatted: String,
    totalTimeFormatted: String,
    progress: Float,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .padding(16.dp)
    ) {
        // Header: العنوان الـ HUD المميز
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CyberHudBadge(
                text = "CYBER HUD PLAYER",
                accentColor = Cyber3DColors.CyanNeon
            )
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        // Center: أزرار التحكم 3D النيون
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPreviousClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Cyber3DColors.DarkGlassSurface)
            ) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Prev", tint = Color.White)
            }

            // زر التشغيل/الإيقاف المضيء 3D
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Cyber3DColors.DarkGlassSurface)
                    .border(
                        2.dp,
                        Brush.linearGradient(listOf(Cyber3DColors.CyanNeon, Cyber3DColors.MagentaNeon)),
                        CircleShape
                    )
                    .clickable(onClick = onPlayPauseClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Cyber3DColors.CyanNeon,
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(
                onClick = onNextClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Cyber3DColors.DarkGlassSurface)
            ) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.White)
            }
        }

        // Bottom: شريط التقدم النيون والوقت
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = currentTimeFormatted, color = Cyber3DColors.CyanNeon, fontSize = 12.sp)
                Text(text = totalTimeFormatted, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
            
            Slider(
                value = progress,
                onValueChange = onSeek,
                colors = SliderDefaults.colors(
                    thumbColor = Cyber3DColors.CyanNeon,
                    activeTrackColor = Cyber3DColors.MagentaNeon,
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                )
            )
        }
    }
}
