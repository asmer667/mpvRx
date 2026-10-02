/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - Folder Card Component
 * بطاقة مجلد بتأثير 3D
 */

package app.gyrolet.mpvrx.ui.cyber.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons

/**
 * بطاقة مجلد بأسلوب Cyber
 */
@Composable
fun CyberFolderCard(
    folderName: String,
    itemCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(120.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Cyber3DColors.GlassSurface)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Cyber3DColors.CyanNeon.copy(alpha = 0.4f),
                        Color.White.copy(alpha = 0.1f),
                    ),
                ),
                shape = RoundedCornerShape(16.dp),
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // الأيقونة
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Cyber3DColors.CyanNeon.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.RoundedFilled.Folder,
                    contentDescription = null,
                    tint = Cyber3DColors.CyanNeon,
                    modifier = Modifier.size(22.dp),
                )
            }

            // النص
            Column {
                Text(
                    text = folderName,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$itemCount عنصر",
                    color = Cyber3DColors.TextTertiary,
                    fontSize = 10.sp,
                )
            }
        }
    }
}
