package app.gyrolet.mpvrx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons
import app.gyrolet.mpvrx.ui.theme.Cyber3DColors

@Composable
fun CyberVideoOptionsSheet(
    video: VideoItem,
    onPlayClick: () -> Unit,
    onShareClick: () -> Unit,
    onInfoClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = video.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            ),
            color = Color.White,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(8.dp))

        OptionItem3D(
            icon = Icons.RoundedFilled.PlayArrow,
            title = "تشغيل الفيديو",
            accentColor = Cyber3DColors.CyanNeon,
            onClick = onPlayClick,
        )

        OptionItem3D(
            icon = Icons.RoundedFilled.Share,
            title = "مشاركة الملف",
            accentColor = Color.White,
            onClick = onShareClick,
        )

        OptionItem3D(
            icon = Icons.RoundedFilled.Info,
            title = "تفاصيل وتنسيق الفيديو",
            accentColor = Cyber3DColors.YellowCyber,
            onClick = onInfoClick,
        )

        OptionItem3D(
            icon = Icons.RoundedFilled.Delete,
            title = "حذف الفيديو",
            accentColor = Cyber3DColors.MagentaNeon,
            onClick = onDeleteClick,
        )
    }
}

@Composable
private fun OptionItem3D(
    icon: ImageVector,
    title: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Cyber3DCard(
        onClick = onClick,
        cornerRadius = 12.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = accentColor,
                modifier = Modifier.size(22.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}