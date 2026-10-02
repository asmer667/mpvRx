/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - Bottom Dock Bar Component
 * الشريط السفلي مع أيقونات التنقل الرئيسية
 */

package app.gyrolet.mpvrx.ui.cyber.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import app.gyrolet.mpvrx.ui.icons.AppIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons

data class CyberDockItem(
    val id: String,
    val label: String,
    val icon: AppIcon,
)

/**
 * الشريط السفلي بتأثير توهج نيوني
 */
@Composable
fun CyberDockBottomBar(
    items: List<CyberDockItem>,
    activeId: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .cyberDockGlow()
            .clip(RoundedCornerShape(30.dp))
            .background(Cyber3DColors.GlassSurfaceStrong)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        Cyber3DColors.CyanNeon.copy(alpha = 0.5f),
                        Cyber3DColors.MagentaNeon.copy(alpha = 0.5f),
                    ),
                ),
                shape = RoundedCornerShape(30.dp),
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                CyberDockItemButton(
                    item = item,
                    isSelected = item.id == activeId,
                    onClick = { onItemClick(item.id) },
                )
            }
        }
    }
}

@Composable
private fun CyberDockItemButton(
    item: CyberDockItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val tint by animateColorAsState(
        targetValue = if (isSelected) Cyber3DColors.CyanNeon else Color.White.copy(alpha = 0.6f),
        label = "dockTint",
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 14.dp, vertical = 6.dp),
    ) {
        // هالة خلفية محددة
        Box(
            modifier = Modifier
                .size(if (isSelected) 38.dp else 32.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) {
                        Brush.radialGradient(
                            listOf(
                                Cyber3DColors.CyanNeon.copy(alpha = 0.35f),
                                Color.Transparent,
                            ),
                        )
                    } else {
                        Brush.radialGradient(listOf(Color.Transparent, Color.Transparent))
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = tint,
                modifier = Modifier.size(22.dp),
            )
        }
        if (isSelected) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.label,
                color = tint,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

/**
 * تأثير توهج للشريط السفلي
 */
private fun Modifier.cyberDockGlow(): Modifier = this.drawBehind {
    drawIntoCanvas { canvas ->
        val paint = NativePaint().apply {
            color = Cyber3DColors.MagentaNeon.copy(alpha = 0.35f).toArgb()
            isAntiAlias = true
            maskFilter = android.graphics.BlurMaskFilter(
                20f,
                android.graphics.BlurMaskFilter.Blur.NORMAL,
            )
        }
        canvas.nativeCanvas.drawRoundRect(
            0f,
            0f,
            size.width,
            size.height,
            30.dp.toPx(),
            30.dp.toPx(),
            paint,
        )
    }
}
