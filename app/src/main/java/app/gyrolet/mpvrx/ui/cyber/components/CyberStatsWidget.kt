/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - Stats Widget Component
 * ودجت التخزين والإحصائيات
 */

package app.gyrolet.mpvrx.ui.cyber.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.cyber.theme.Cyber3DColors

/**
 * ودجت الإحصائيات مع حلقة تقدم دائرية
 */
@Composable
fun CyberStatsWidget(
    title: String,
    usedText: String,
    totalText: String,
    percentage: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.Black.copy(alpha = 0.5f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.12f),
                shape = RoundedCornerShape(18.dp),
            )
            .padding(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // حلقة التقدم
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(48.dp),
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 4.5.dp.toPx()
                    // الخلفية
                    drawArc(
                        color = Color.White.copy(alpha = 0.12f),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(strokeWidth),
                    )
                    // التقدم
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(
                                Cyber3DColors.CyanNeon,
                                Cyber3DColors.MagentaNeon,
                                Cyber3DColors.CyanNeon,
                            ),
                        ),
                        startAngle = -90f,
                        sweepAngle = percentage * 360f,
                        useCenter = false,
                        style = Stroke(strokeWidth, cap = StrokeCap.Round),
                    )
                }
                Text(
                    text = "${(percentage * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                )
            }

            // النص
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$usedText / $totalText",
                    color = Cyber3DColors.TextTertiary,
                    fontSize = 9.sp,
                )
            }
        }
    }
}
