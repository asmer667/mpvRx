/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - Top App Bar Component
 * الشريط العلوي مع أزرار الإجراءات
 */

package app.gyrolet.mpvrx.ui.cyber.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import app.gyrolet.mpvrx.ui.icons.AppIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons

/**
 * الشريط العلوي بأسلوب Cyber
 */
@Composable
fun CyberTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit = {},
    onThemeToggleClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = Cyber3DColors.TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CyberIconButton(
                icon = Icons.RoundedFilled.Search,
                contentDescription = "Search",
                onClick = onSearchClick,
            )
            CyberIconButton(
                icon = Icons.RoundedFilled.Brightness6,
                contentDescription = "Theme",
                onClick = onThemeToggleClick,
            )
            CyberIconButton(
                icon = Icons.RoundedFilled.Settings,
                contentDescription = "Settings",
                onClick = onSettingsClick,
            )
        }
    }
}

@Composable
private fun CyberIconButton(
    icon: AppIcon,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(Cyber3DColors.GlassSurface)
            .border(1.dp, Cyber3DColors.DarkGlassBorder, CircleShape),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(16.dp),
        )
    }
}
