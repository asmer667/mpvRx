package app.gyrolet.mpvrx.ui.components

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
import app.gyrolet.mpvrx.ui.theme.Cyber3DColors

<<<<<<< HEAD
/**
 * لوحة ألوان إضافية جديدة (Cyber3DColorsExtra)
 * لا تتعارض مع Cyber3DColors الموجود في CyberTheme.kt
 */
object Cyber3DColorsExtra {
    // الألوان النيونية الأساسية
    val CyanNeon = Color(0xFF00E5FF)
    val MagentaNeon = Color(0xFFFF00AA)
    val PurpleNeon = Color(0xFF9D00FF)
    val YellowCyber = Color(0xFFFFD500)
=======
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
>>>>>>> cd3603c (Complete Cyber UI refactor: fix imports, remove old files)

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

    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
                this.rotationX = rotationX
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
