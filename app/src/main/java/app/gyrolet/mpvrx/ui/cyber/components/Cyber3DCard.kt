/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - 3D Card Component
 * بطاقة ثلاثية الأبعاد بتأثير ضغط وإضاءة حافة متوهجة
 */

package app.gyrolet.mpvrx.ui.cyber.components

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
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors

/**
 * بطاقة ثلاثية الأبعاد بتأثير:
 * - ضغط (scale down عند اللمس)
 * - دوران طفيف (rotationX)
 * - إضاءة الحافة العلوية (bevel highlight)
 * - حدود نيون متدرجة
 *
 * @param onClick دالة الضغط
 * @param modifier المُعدِّل
 * @param isSelected هل البطاقة محددة (تستخدم حدود نيون قوية)
 * @param cornerRadius نصف قطر الزوايا
 * @param content المحتوى الداخلي
 */
@Composable
fun Cyber3DCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    cornerRadius: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // تأثير الضغط: التصغير
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "cardScale",
    )

    // تأثير الضغط: الميل للأمام (rotationX)
    val rotationX by animateFloatAsState(
        targetValue = if (isPressed) 4f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "cardRotationX",
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
                shape = shape,
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                brush = if (isSelected) {
                    Cyber3DColors.BevelNeonGradient
                } else {
                    Cyber3DColors.BevelLightGradient
                },
                shape = shape,
            )
            .drawWithContent {
                drawContent()
                // إضاءة الحافة العلوية لمحاكاة بروز 3D
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.Transparent,
                        ),
                        startY = 0f,
                        endY = size.height * 0.3f,
                    ),
                )
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        content = content,
    )
}
