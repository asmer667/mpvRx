/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - Sidebar Component
 * الشريط الجانبي مع عناصر التنقل
 */

package app.gyrolet.mpvrx.ui.cyber.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import app.gyrolet.mpvrx.ui.icons.AppIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons

data class CyberSidebarItem(
    val id: String,
    val title: String,
    val icon: AppIcon,
)

/**
 * الشريط الجانبي بأسلوب Cyber مع توهج نيوني
 */
@Composable
fun CyberSidebar(
    items: List<CyberSidebarItem>,
    activeId: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    headerTitle: String = "mpvRx",
    headerIcon: AppIcon = Icons.RoundedFilled.PlayCircle,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Cyber3DColors.GlassSurface)
            .border(1.dp, Cyber3DColors.DarkGlassBorder, RoundedCornerShape(26.dp))
            .padding(14.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 14.dp),
            ) {
                Icon(
                    imageVector = headerIcon,
                    contentDescription = null,
                    tint = Cyber3DColors.CyanNeon,
                    modifier = Modifier.size(30.dp),
                )
                Text(
                    text = headerTitle,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Items
            items.forEach { item ->
                CyberSidebarItemRow(
                    item = item,
                    isSelected = item.id == activeId,
                    onClick = { onItemClick(item.id) },
                )
            }
        }
    }
}

@Composable
private fun CyberSidebarItemRow(
    item: CyberSidebarItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Cyber3DColors.CyanNeon.copy(alpha = 0.2f) else Color.Transparent,
        label = "bg",
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Cyber3DColors.TextSecondary,
        label = "text",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .background(bgColor)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) Cyber3DColors.CyanNeon.copy(alpha = 0.6f) else Color.Transparent,
                shape = RoundedCornerShape(14.dp),
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = if (isSelected) Cyber3DColors.CyanNeon else Cyber3DColors.TextSecondary,
            modifier = Modifier.size(19.dp),
        )
        Text(
            text = item.title,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        )
    }
}
