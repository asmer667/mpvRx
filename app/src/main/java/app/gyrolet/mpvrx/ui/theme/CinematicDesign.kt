package app.gyrolet.mpvrx.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Shared visual language used by the final local-media experience. */
object CinematicDesign {
  val Background = Color(0xFF03040B)
  val Surface = Color(0xFF080B17)
  val Surface2 = Color(0xFF0D1222)
  val Surface3 = Color(0xFF151B31)
  val Purple = Color(0xFF8B5CFF)
  val Blue = Color(0xFF24BFFF)
  val Pink = Color(0xFFE64DFF)
  val Orange = Color(0xFFFF8A45)
  val Green = Color(0xFF49E6A1)
  val TextMuted = Color(0xFF9BA7C7)

  val AccentGradient = Brush.linearGradient(listOf(Purple, Blue, Pink))
  val SoftGradient = Brush.linearGradient(listOf(Purple.copy(.28f), Blue.copy(.16f), Surface))

  @Composable
  fun ambientModifier(): Modifier {
    val transition = rememberInfiniteTransition(label = "cinematic-ambient")
    val shift by transition.animateFloat(
      initialValue = 0f,
      targetValue = 1f,
      animationSpec = infiniteRepeatable(tween(9000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
      label = "ambient-shift",
    )
    return Modifier
      .background(
        Brush.radialGradient(
          colors = listOf(Purple.copy(alpha = .08f + .03f * shift), Background, Background),
          radius = 900f,
        ),
      )
      .drawBehind {
        // Shared flowing ribbons: these appear behind Home, folders and episode lists so
        // every level of the local library speaks the same visual language.
        val top = androidx.compose.ui.graphics.Path().apply {
          moveTo(-40f, size.height * .045f)
          cubicTo(size.width * .18f, size.height * .11f, size.width * .30f, -8f, size.width * .48f, size.height * .038f)
          cubicTo(size.width * .68f, size.height * .11f, size.width * .80f, size.height * .015f, size.width + 40f, size.height * .075f)
        }
        drawPath(
          top,
          brush = Brush.horizontalGradient(listOf(Color.Transparent, Blue.copy(.28f + .12f * shift), Purple.copy(.36f + .12f * shift), Pink.copy(.28f + .10f * shift), Color.Transparent)),
          style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5.dp.toPx()),
        )
        val bottom = androidx.compose.ui.graphics.Path().apply {
          moveTo(-40f, size.height * .94f)
          cubicTo(size.width * .18f, size.height * .87f, size.width * .31f, size.height * .98f, size.width * .49f, size.height * .91f)
          cubicTo(size.width * .69f, size.height * .84f, size.width * .81f, size.height * .98f, size.width + 40f, size.height * .90f)
        }
        drawPath(
          bottom,
          brush = Brush.horizontalGradient(listOf(Color.Transparent, Pink.copy(.22f + .10f * shift), Blue.copy(.30f + .10f * shift), Purple.copy(.32f + .10f * shift), Color.Transparent)),
          style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()),
        )
      }
  }
}

@Composable
fun CinematicPanel(
  modifier: Modifier = Modifier,
  accent: Color = CinematicDesign.Purple,
  content: @Composable () -> Unit,
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(24.dp))
      .background(CinematicDesign.Surface.copy(alpha = .96f))
      .border(1.dp, accent.copy(alpha = .28f), RoundedCornerShape(24.dp)),
    contentAlignment = Alignment.Center,
  ) { content() }
}

@Composable
fun CinematicPulse(
  modifier: Modifier = Modifier,
  accent: Color = CinematicDesign.Purple,
) {
  val transition = rememberInfiniteTransition(label = "cinematic-pulse")
  val alpha by transition.animateFloat(
    initialValue = .12f,
    targetValue = .42f,
    animationSpec = infiniteRepeatable(tween(1700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "pulse-alpha",
  )
  Box(
    modifier = modifier
      .border(1.dp, accent.copy(alpha = alpha), RoundedCornerShape(22.dp))
      .clip(RoundedCornerShape(22.dp))
      .background(accent.copy(alpha = alpha * .16f)),
  )
}

@Composable
fun CinematicDivider(modifier: Modifier = Modifier, accent: Color = CinematicDesign.Purple) {
  Spacer(
    modifier = modifier
      .fillMaxWidth()
      .height(1.dp)
      .background(Brush.horizontalGradient(listOf(Color.Transparent, accent.copy(.65f), CinematicDesign.Blue.copy(.4f), Color.Transparent))),
  )
}
