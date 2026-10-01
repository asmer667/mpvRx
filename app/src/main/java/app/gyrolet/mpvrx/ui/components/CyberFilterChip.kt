package app.gyrolet.mpvrx.ui.components // عدل اسم الـ package ليطابق مشروعك إذا كان مختلفاً

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.video.ui.theme.Cyber3DColors

@Composable
fun CyberFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Cyber3DColors.CyanNeon else Color.White.copy(alpha = 0.2f),
        label = "chipBorder"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Cyber3DColors.CyanNeon else Color.White.copy(alpha = 0.7f),
        label = "chipText"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) Cyber3DColors.DarkGlassSurface else Color.Black.copy(alpha = 0.3f)
            )
            .border(
                width = 1.dp,
                brush = if (isSelected) {
                    Brush.horizontalGradient(listOf(Cyber3DColors.CyanNeon, Cyber3DColors.MagentaNeon))
                } else {
                    Brush.linearGradient(listOf(borderColor, Color.Transparent))
                },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
