/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - HUD Badge Component
 * شارة HUD صغيرة للاستخدام على البطاقات والأزرار
 */

package app.gyrolet.mpvrx.ui.cyber.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors

/**
 * شارة HUD بأسلوب سايبربانك
 *
 * @param text النص
 * @param modifier المُعدِّل
 * @param accentColor لون التمييز
 * @param backgroundColor لون الخلفية
 */
@Composable
fun CyberHudBadge(
    text: String,
    modifier: Modifier = Modifier,
    accentColor: Color = Cyber3DColors.CyanNeon,
    backgroundColor: Color = Cyber3DColors.DarkGlassSurface,
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(6.dp),
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        accentColor.copy(alpha = 0.9f),
                        accentColor.copy(alpha = 0.2f),
                    ),
                ),
                shape = RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                letterSpacing = 1.2.sp,
            ),
            color = accentColor,
        )
    }
}
