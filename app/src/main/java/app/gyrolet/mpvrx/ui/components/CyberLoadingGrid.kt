package app.gyrolet.mpvrx.ui.components // عدل اسم الـ package ليطابق مشروعك إذا كان مختلفاً

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.video.ui.theme.Cyber3DColors

@Composable
fun CyberLoadingGrid(
    modifier: Modifier = Modifier,
    isGrid: Boolean = true
) {
    val transition = rememberInfiniteTransition(label = "cyber_shimmer")
    val alpha by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    if (isGrid) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = modifier.fillMaxSize().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(8) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Cyber3DColors.DarkGlassSurface)
                        .border(
                            1.dp,
                            Brush.linearGradient(
                                listOf(
                                    Cyber3DColors.CyanNeon.copy(alpha = alpha),
                                    Color.Transparent
                                )
                            ),
                            RoundedCornerShape(16.dp)
                        )
                )
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(6) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Cyber3DColors.DarkGlassSurface)
                        .border(
                            1.dp,
                            Brush.horizontalGradient(
                                listOf(
                                    Cyber3DColors.MagentaNeon.copy(alpha = alpha),
                                    Color.Transparent
                                )
                            ),
                            RoundedCornerShape(16.dp)
                        )
                )
            }
        }
    }
}
