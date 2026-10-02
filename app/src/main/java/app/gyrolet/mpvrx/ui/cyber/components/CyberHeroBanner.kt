/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - Hero Banner Component
 * البطاقة الكبيرة العلوية للمحتوى المميز
 */

package app.gyrolet.mpvrx.ui.cyber.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons

/**
 * بطاقة Hero كبيرة بتأثير 3D وتوهج
 */
@Composable
fun CyberHeroBanner(
    title: String,
    subtitle: String? = null,
    thumbnail: Bitmap? = null,
    duration: String? = null,
    badge: String? = null,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .cyberHeroGlow()
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        Cyber3DColors.CyanNeon,
                        Cyber3DColors.MagentaNeon,
                        Cyber3DColors.PurpleNeon,
                    ),
                ),
                shape = RoundedCornerShape(24.dp),
            )
            .background(Cyber3DColors.GlassSurface),
    ) {
        // الصورة المصغرة
        thumbnail?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        // Overlay تدرج داكن للنص
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.88f),
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )

        // المحتوى
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
        ) {
            // شارة
            badge?.let {
                CyberHudBadge(
                    text = it,
                    accentColor = Cyber3DColors.MagentaNeon,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // العنوان
            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            // العنوان الفرعي
            subtitle?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    color = Cyber3DColors.TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // الأزرار
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // زر التشغيل
                Button(
                    onClick = onPlayClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Cyber3DColors.CyanNeon,
                                Cyber3DColors.PurpleNeon,
                            ),
                        ),
                        shape = RoundedCornerShape(14.dp),
                    ),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Icon(
                            imageVector = Icons.RoundedFilled.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = "تشغيل الآن",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                        )
                    }
                }

                // المدة
                duration?.let {
                    CyberHudBadge(
                        text = it,
                        accentColor = Cyber3DColors.CyanNeon,
                    )
                }
            }
        }
    }
}

private fun Modifier.cyberHeroGlow(): Modifier = this.drawBehind {
    drawIntoCanvas { canvas ->
        val paint = NativePaint().apply {
            color = Cyber3DColors.PurpleNeon.copy(alpha = 0.5f).toArgb()
            isAntiAlias = true
            maskFilter = android.graphics.BlurMaskFilter(
                24f,
                android.graphics.BlurMaskFilter.Blur.NORMAL,
            )
        }
        canvas.nativeCanvas.drawRoundRect(
            0f,
            0f,
            size.width,
            size.height,
            24.dp.toPx(),
            24.dp.toPx(),
            paint,
        )
    }
}
