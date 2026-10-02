/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - Hero Stack Component
 * البطاقة الرئيسية + بطاقات مصغرة مائلة بجانبها
 * تشير إلى الفيديوهات السابقة (Swipe للتنقل)
 */

package app.gyrolet.mpvrx.ui.cyber.components

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons
import kotlin.math.abs

/**
 * عنصر الفيديو في الـ Hero Stack
 */
data class CyberHeroItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val duration: String? = null,
    val badge: String? = null,
    val thumbnail: Bitmap? = null,
)

/**
 * Hero Stack - البطاقة الرئيسية + بطاقات مصغرة مائلة بجانبها
 *
 * @param items قائمة الفيديوهات (الأول هو الرئيسي، الباقي مصغرة مائلة بجانبه)
 * @param onPlayClick عند الضغط على زر التشغيل في البطاقة الرئيسية
 * @param onItemClick عند الضغط على بطاقة مصغرة (للانتقال إليها)
 * @param maxStackSize عدد البطاقات المصغرة (افتراضي 4)
 */
@Composable
fun CyberHeroStack(
    items: List<CyberHeroItem>,
    onPlayClick: (String) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxStackSize: Int = 4,
) {
    if (items.isEmpty()) return

    val currentItem = items.firstOrNull() ?: return
    val stackItems = items.drop(1).take(maxStackSize)

    // حالة السحب
    var dragOffset by remember { mutableFloatStateOf(0f) }

    // أنيميشن العودة
    val animatedOffset by animateFloatAsState(
        targetValue = dragOffset,
        animationSpec = tween(300),
        label = "heroSwipeOffset",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (abs(dragOffset) > 150f) {
                            // انتقل للفيديو التالي
                            items.getOrNull(1)?.let { onItemClick(it.id) }
                        }
                        dragOffset = 0f
                    },
                    onHorizontalDrag = { _, delta ->
                        dragOffset += delta
                    },
                )
            },
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // ─────────────────────────────────────────
            // Hero الرئيسي (يسار - 62%)
            // ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .weight(0.62f)
                    .fillMaxHeight()
                    .graphicsLayer {
                        translationX = animatedOffset * 0.3f
                    },
            ) {
                CyberHeroBanner(
                    title = currentItem.title,
                    subtitle = currentItem.subtitle,
                    thumbnail = currentItem.thumbnail,
                    duration = currentItem.duration,
                    badge = currentItem.badge,
                    onPlayClick = { onPlayClick(currentItem.id) },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            // ─────────────────────────────────────────
            // البطاقات المصغرة المائلة (يمين - 38%)
            // ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .weight(0.38f)
                    .fillMaxHeight()
                    .graphicsLayer {
                        translationX = animatedOffset * 0.7f
                    },
            ) {
                stackItems.forEachIndexed { index, item ->
                    val rotationAngle = -(index * 8f)  // -8°, -16°, -24°, -32°
                    val offsetX = index * 22f
                    val offsetY = index * 4f
                    val scale = 1f - (index * 0.05f)
                    val alpha = 1f - (index * 0.15f)

                    CyberStackCard(
                        item = item,
                        rotationZ = rotationAngle,
                        offsetX = offsetX,
                        offsetY = offsetY,
                        scale = scale,
                        alpha = alpha,
                        zIndex = (stackItems.size - index).toFloat(),
                        onClick = { onItemClick(item.id) },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

/**
 * بطاقة مصغرة واحدة في الـ Stack
 */
@Composable
private fun CyberStackCard(
    item: CyberHeroItem,
    rotationZ: Float,
    offsetX: Float,
    offsetY: Float,
    scale: Float,
    alpha: Float,
    zIndex: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                this.translationX = offsetX
                this.translationY = offsetY
                this.rotationZ = rotationZ
                this.scaleX = scale
                this.scaleY = scale
                this.alpha = alpha

                cameraDistance = 12f * density
            }
            .stackCardGlow(rotationZ)
            .clip(RoundedCornerShape(16.dp))
            .background(Cyber3DColors.GlassSurfaceStrong)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Cyber3DColors.CyanNeon.copy(alpha = 0.6f),
                        Cyber3DColors.MagentaNeon.copy(alpha = 0.4f),
                        Color.Transparent,
                    ),
                ),
                shape = RoundedCornerShape(16.dp),
            )
            .clickable(onClick = onClick),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            item.thumbnail?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } ?: Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Cyber3DColors.PurpleNeon.copy(alpha = 0.3f),
                                Cyber3DColors.BackgroundDark,
                            ),
                        ),
                    ),
            )

            // Overlay داكن
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f),
                            ),
                        ),
                    ),
            )
        }

        // المحتوى
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            item.duration?.let {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = it,
                    color = Cyber3DColors.CyanNeon,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }

        // أيقونة تشغيل صغيرة
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(Cyber3DColors.CyanNeon.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.RoundedFilled.PlayArrow,
                contentDescription = null,
                tint = Cyber3DColors.CyanNeon,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

/**
 * توهج حول البطاقة المصغرة
 */
private fun Modifier.stackCardGlow(rotation: Float): Modifier = this.drawBehind {
    drawIntoCanvas { canvas ->
        val color = if (rotation < -15f) {
            Cyber3DColors.MagentaNeon.copy(alpha = 0.4f)
        } else {
            Cyber3DColors.CyanNeon.copy(alpha = 0.35f)
        }
        val paint = NativePaint().apply {
            this.color = color.toArgb()
            isAntiAlias = true
            maskFilter = android.graphics.BlurMaskFilter(
                14f,
                android.graphics.BlurMaskFilter.Blur.NORMAL,
            )
        }
        canvas.nativeCanvas.drawRoundRect(
            0f,
            0f,
            size.width,
            size.height,
            16.dp.toPx(),
            16.dp.toPx(),
            paint,
        )
    }
}
