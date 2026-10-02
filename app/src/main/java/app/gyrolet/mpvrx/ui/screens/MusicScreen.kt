package app.gyrolet.mpvrx.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.gyrolet.mpvrx.ui.icons.Icon
import app.gyrolet.mpvrx.ui.icons.Icons

@Composable
fun MusicScreen(
    currentTrackTitle: String = "Cyberpunk Synthwave - Track 01",
    artistName: String = "mpvRx Audio Engine",
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var trackProgress by remember { mutableFloatStateOf(0.35f) }
    var isShuffle by remember { mutableStateOf(false) }
    var isRepeat by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFF030108))) {
        // خلفيات محيطية متوهجة
        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.Center)
                .blur(120.dp)
                .background(Color(0xFF8A00FF).copy(0.25f), CircleShape)
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "مشغل الصوتيات النيوني",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            // غلاف الألبوم الزجاجي ثلاثي الأبعاد
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .border(
                        2.dp,
                        Brush.linearGradient(listOf(Color(0xFF00F0FF), Color(0xFFFF007A))),
                        RoundedCornerShape(32.dp)
                    )
                    .background(Color(0x221A103C))
            ) {
                Icon(
                    imageVector = Icons.RoundedFilled.Audiotrack,
                    contentDescription = null,
                    tint = Color(0xFF00F0FF),
                    modifier = Modifier.size(80.dp)
                )
            }

            // الموجات الصوتية التفاعلية
            AnimatedSpectrumVisualizer(isPlaying = isPlaying)

            // معلومات المقطع
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    currentTrackTitle,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(artistName, color = Color(0xCCFFFFFF), fontSize = 12.sp)
            }

            // شريط تقدم الأغنية
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = trackProgress,
                    onValueChange = { trackProgress = it },
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF00F0FF),
                        activeTrackColor = Color(0xFF00F0FF),
                        inactiveTrackColor = Color.White.copy(0.15f)
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("01:24", color = Color(0xCCFFFFFF), fontSize = 10.sp)
                    Text("03:45", color = Color(0xCCFFFFFF), fontSize = 10.sp)
                }
            }

            // أزرار التحكم
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isShuffle = !isShuffle }) {
                    Icon(
                        imageVector = Icons.RoundedFilled.Shuffle,
                        contentDescription = "خلط",
                        tint = if (isShuffle) Color(0xFF00F0FF) else Color.Gray
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.RoundedFilled.SkipPrevious,
                        contentDescription = "السابق",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF00F0FF), Color(0xFF8A00FF))
                            )
                        )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.RoundedFilled.Pause else Icons.RoundedFilled.PlayArrow,
                        contentDescription = "تشغيل/إيقاف",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.RoundedFilled.SkipNext,
                        contentDescription = "التالي",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(onClick = { isRepeat = !isRepeat }) {
                    Icon(
                        imageVector = Icons.RoundedFilled.Repeat,
                        contentDescription = "تكرار",
                        tint = if (isRepeat) Color(0xFFFF007A) else Color.Gray
                    )
                }
            }
        }
    }
}

// رسم الموجات الصوتية النيونية التفاعلية
@Composable
private fun AnimatedSpectrumVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "spectrum")

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(40.dp)
    ) {
        repeat(12) { index ->
            val heightFraction by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = if (isPlaying) 1f else 0.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 300 + (index * 60),
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "barHeight"
            )
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight(heightFraction)
                    .clip(CircleShape)
                    .background(
                        if (index % 2 == 0) Color(0xFF00F0FF) else Color(0xFFFF007A)
                    )
            )
        }
    }
}