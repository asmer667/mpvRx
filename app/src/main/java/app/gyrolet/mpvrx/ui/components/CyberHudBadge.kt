package app.gyrolet.mpvrx.ui.components // عدل اسم الـ package ليطابق مشروعك إذا كان مختلفاً

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.video.ui.theme.Cyber3DColors

@Composable
fun CyberHudBadge(
    text: String,
    modifier: Modifier = Modifier,
    accentColor: Color = Cyber3DColors.CyanNeon,
    backgroundColor: Color = Cyber3DColors.DarkGlassSurface
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        accentColor.copy(alpha = 0.9f),
                        accentColor.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                letterSpacing = 1.2.sp
            ),
            color = accentColor
        )
    }
}
