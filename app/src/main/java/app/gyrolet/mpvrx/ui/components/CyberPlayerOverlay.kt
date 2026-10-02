package app.gyrolet.mpvrx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons
import app.gyrolet.mpvrx.ui.theme.Cyber3DColors

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
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CyberHudBadge(
                text = "CYBER HUD PLAYER",
                accentColor = Cyber3DColors.CyanNeon,
            )
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onPreviousClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Cyber3DColors.DarkGlassSurface),
            ) {
                Icon(
                    imageVector = Icons.RoundedFilled.SkipPrevious,
                    contentDescription = "Prev",
                    tint = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Cyber3DColors.DarkGlassSurface)
                    .border(
                        2.dp,
                        Brush.linearGradient(listOf(Cyber3DColors.CyanNeon, Cyber3DColors.MagentaNeon)),
                        CircleShape,
                    )
                    .clickable(onClick = onPlayPauseClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.RoundedFilled.Pause else Icons.RoundedFilled.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Cyber3DColors.CyanNeon,
                    modifier = Modifier.size(36.dp),
                )
            }

            IconButton(
                onClick = onNextClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Cyber3DColors.DarkGlassSurface),
            ) {
                Icon(
                    imageVector = Icons.RoundedFilled.SkipNext,
                    contentDescription = "Next",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f),
                ),
            )
        }
    }
}