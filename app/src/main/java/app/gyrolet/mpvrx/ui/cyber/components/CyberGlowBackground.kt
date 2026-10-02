/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - Glow Background Component
 * خلفية متوهجة بطبقات نيونية متعددة
 */

package app.gyrolet.mpvrx.ui.cyber.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors

/**
 * خلفية متوهجة بألوان نيونية
 * - خلفية داكنة أساسية
 * - توهج بنفسجي أعلى يسار
 * - توهج سماوي أسفل يمين
 * - توهج وردي في المنتصف
 */
@Composable
fun CyberGlowBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Cyber3DColors.BackgroundDark),
    ) {
        // توهج بنفسجي - أعلى يسار
        Box(
            modifier = Modifier
                .size(500.dp)
                .align(Alignment.TopStart)
                .blur(160.dp)
                .background(Cyber3DColors.PurpleNeon.copy(alpha = 0.35f), CircleShape),
        )

        // توهج سماوي - أسفل يمين
        Box(
            modifier = Modifier
                .size(450.dp)
                .align(Alignment.BottomEnd)
                .blur(150.dp)
                .background(Cyber3DColors.CyanNeon.copy(alpha = 0.22f), CircleShape),
        )

        // توهج وردي - منتصف
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center)
                .blur(180.dp)
                .background(Cyber3DColors.MagentaNeon.copy(alpha = 0.12f), CircleShape),
        )

        content()
    }
}
