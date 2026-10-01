package app.gyrolet.mpvrx.ui.components // عدل اسم الـ package ليطابق مشروعك إذا كان مختلفاً

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.video.ui.theme.Cyber3DColors

@Composable
fun Cyber3DCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    cornerRadius: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // تأثير ضغط العمق ثلاثي الأبعاد (3D Press Effect)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "cardScale"
    )
    
    val rotationX by animateFloatAsState(
        targetValue = if (isPressed) 4f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "cardRotationX"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isSelected) 20f else if (isPressed) 4f else 12f,
        animationSpec = tween(durationMillis = 150),
        label = "cardElevation"
    )

    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
                this.rotationX = rotationX
                this.shadowElevation = shadowElevation
                this.shape = shape
                this.clip = false
            }
            .background(
                brush = Cyber3DColors.CardBackgroundGradient,
                shape = shape
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                brush = if (isSelected) Cyber3DColors.BevelNeonGradient else Cyber3DColors.BevelLightGradient,
                shape = shape
            )
            .drawWithContent {
                drawContent()
                // إضاءة الحافة العلوية لتمثيل بروز 3D
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0.15f), Color.Transparent),
                        startY = 0f,
                        endY = size.height * 0.3f
                    )
                )
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        content = content
    )
}
