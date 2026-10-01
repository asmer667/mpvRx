package app.gyrolet.mpvrx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.gyrolet.mpvrx.ui.theme.Cyber3DColors

@Composable
fun CyberVideoDetailsDialog(
    video: VideoItem,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Cyber3DColors.DarkGlassSurface)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(Cyber3DColors.CyanNeon, Cyber3DColors.MagentaNeon),
                    ),
                    shape = RoundedCornerShape(24.dp),
                )
                .padding(20.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "تفاصيل الملف الـ HUD",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                        ),
                        color = Color.White,
                    )

                    CyberHudBadge(
                        text = video.resolution.ifEmpty { "MEDIA" },
                        accentColor = Cyber3DColors.CyanNeon,
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                DetailItemRow(label = "اسم الملف", value = video.title)
                DetailItemRow(label = "الحجم", value = video.sizeFormatted)
                DetailItemRow(label = "المدة الزمنية", value = video.durationFormatted)
                DetailItemRow(label = "المسار", value = video.path)

                Spacer(modifier = Modifier.height(8.dp))

                Cyber3DCard(
                    onClick = onDismissRequest,
                    cornerRadius = 12.dp,
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(
                        text = "إغلاق",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        color = Cyber3DColors.CyanNeon,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailItemRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Cyber3DColors.CyanNeon.copy(alpha = 0.8f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 13.sp,
        )
    }
}