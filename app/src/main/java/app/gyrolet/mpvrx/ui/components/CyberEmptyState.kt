package app.gyrolet.mpvrx.ui.components // عدل اسم الـ package ليطابق مشروعك إذا كان مختلفاً

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.video.ui.theme.Cyber3DColors

@Composable
fun CyberEmptyState(
    message: String = "لا توجد ملفات فيديو مسجلة",
    onRefreshClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // دائرة توهج ثلاثية الأبعاد مع أيقونة الميديا
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Cyber3DColors.PurpleNeon.copy(alpha = 0.4f),
                            Cyber3DColors.DarkGlassSurface
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.VideoLibrary,
                contentDescription = null,
                tint = Cyber3DColors.CyanNeon,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        CyberHudBadge(
            text = "SYSTEM STATUS: EMPTY",
            accentColor = Cyber3DColors.YellowCyber
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            ),
            color = Color.White.copy(alpha = 0.7f)
        )

        if (onRefreshClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Cyber3DCard(
                onClick = onRefreshClick,
                cornerRadius = 12.dp
            ) {
                Text(
                    text = "إعادة مسح الميديا",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    color = Cyber3DColors.CyanNeon,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
