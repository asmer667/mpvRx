/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - Video Card Component
 * بطاقة فيديو ثلاثية الأبعاد مع طبقات
 */

package app.gyrolet.mpvrx.ui.cyber.components

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons

/**
 * بطاقة فيديو 3D بطبقات متراكبة
 */
@Composable
fun CyberVideoCard(
    title: String,
    thumbnail: Bitmap?,
    duration: String? = null,
    resolution: String? = null,
    isFavorite: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: androidx.compose.ui.unit.Dp = 160.dp,
    height: androidx.compose.ui.unit.Dp = 220.dp,
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(150),
        label = "videoCardScale",
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    isPressed = false
                    onClick()
                },
            ),
    ) {
        // الطبقة الخلفية - ظل بنفسجي
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Cyber3DColors.PurpleNeon.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(18.dp),
                )
                .border(
                    width = 1.dp,
                    color = Cyber3DColors.PurpleNeon.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(18.dp),
                ),
        )

        // الطبقة الوسطى - ظل وردي
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Cyber3DColors.MagentaNeon.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(18.dp),
                )
                .border(
                    width = 1.dp,
                    color = Cyber3DColors.MagentaNeon.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(18.dp),
                ),
        )

        // الطبقة الأمامية - البطاقة الرئيسية
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(18.dp))
                .background(Cyber3DColors.GlassSurfaceStrong)
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Cyber3DColors.CyanNeon,
                            Cyber3DColors.MagentaNeon,
                            Color.Transparent,
                        ),
                    ),
                    shape = RoundedCornerShape(18.dp),
                ),
        ) {
            // الصورة
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(),
            ) {
                thumbnail?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } ?: Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                )

                // Overlay تدرج داكن
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.1f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.95f),
                                ),
                            ),
                        ),
                )
            }

            // شارة المفضلة
            if (isFavorite) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .border(1.dp, Cyber3DColors.MagentaNeon.copy(alpha = 0.8f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.RoundedFilled.Favorite,
                        contentDescription = null,
                        tint = Cyber3DColors.MagentaNeon,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            // المحتوى في الأسفل
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                // العنوان
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(6.dp))

                // الشارات السفلية
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    duration?.let {
                        CyberHudBadge(
                            text = it,
                            accentColor = Cyber3DColors.CyanNeon,
                        )
                    }
                    resolution?.let {
                        CyberHudBadge(
                            text = it,
                            accentColor = Cyber3DColors.MagentaNeon,
                        )
                    }
                }
            }

            // زر تشغيل عائم عند الوسط
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Cyber3DColors.CyanNeon.copy(alpha = 0.25f))
                    .border(1.5.dp, Cyber3DColors.CyanNeon, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.RoundedFilled.PlayArrow,
                    contentDescription = null,
                    tint = Cyber3DColors.CyanNeon,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}
