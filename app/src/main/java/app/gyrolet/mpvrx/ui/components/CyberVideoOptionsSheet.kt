package app.gyrolet.mpvrx.ui.components // عدل اسم الـ package ليطابق مشروعك إذا كان مختلفاً

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.video.model.VideoItem
import com.app.video.ui.theme.Cyber3DColors

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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // عنوان المقطع المعروض
        Text(
            text = video.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            color = Color.White,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(8.dp))

        // خيارات المقطع على شكل بطاقات 3D
        OptionItem3D(
            icon = Icons.Default.PlayArrow,
            title = "تشغيل الفيديو",
            accentColor = Cyber3DColors.CyanNeon,
            onClick = onPlayClick
        )

        OptionItem3D(
            icon = Icons.Default.Share,
            title = "مشاركة الملف",
            accentColor = Color.White,
            onClick = onShareClick
        )

        OptionItem3D(
            icon = Icons.Default.Info,
            title = "تفاصيل وتنسيق الفيديو",
            accentColor = Cyber3DColors.YellowCyber,
            onClick = onInfoClick
        )

        OptionItem3D(
            icon = Icons.Default.Delete,
            title = "حذف الفيديو",
            accentColor = Cyber3DColors.MagentaNeon,
            onClick = onDeleteClick
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = accentColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
