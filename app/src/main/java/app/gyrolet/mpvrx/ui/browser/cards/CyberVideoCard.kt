package app.gyrolet.mpvrx.ui.browser.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.components.Cyber3DCard
import app.gyrolet.mpvrx.ui.components.CyberHudBadge
import app.gyrolet.mpvrx.ui.components.VideoItem
import app.gyrolet.mpvrx.ui.theme.Cyber3DColors

@Composable
fun CyberVideoCard(
    video: VideoItem,
    isGrid: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Cyber3DCard(
        onClick = onClick,
        cornerRadius = 14.dp,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // أيقونة التشغيل
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Cyber3DColors.CyanNeon,
                    modifier = Modifier.size(24.dp),
                )
            }

            Column {
                Text(
                    text = video.title.ifBlank { "بدون عنوان" },
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (video.durationFormatted.isNotBlank()) {
                        CyberHudBadge(
                            text = video.durationFormatted,
                            accentColor = Cyber3DColors.CyanNeon,
                        )
                    }
                    if (video.resolution.isNotBlank()) {
                        CyberHudBadge(
                            text = video.resolution,
                            accentColor = Cyber3DColors.MagentaNeon,
                        )
                    }
                }
            }
        }
    }
}