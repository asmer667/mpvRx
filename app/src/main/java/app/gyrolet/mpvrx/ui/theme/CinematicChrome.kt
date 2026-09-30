/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Shared visual chrome for the local cinematic media experience.
 */
package app.gyrolet.mpvrx.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * The shared stage behind Home, Music, Recents, Playlists and browser screens.
 * It deliberately contains no media content: all content remains local and dynamic.
 */
@Composable
fun CinematicScreenChrome(
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit,
) {
  val transition = rememberInfiniteTransition(label = "cinematic-screen-chrome")
  val phase by transition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(tween(9000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "chrome-phase",
  )
  val glow by transition.animateFloat(
    initialValue = .42f,
    targetValue = .82f,
    animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "chrome-glow",
  )

  Box(
    modifier = modifier.background(
      Brush.verticalGradient(
        listOf(
          CinematicDesign.Background,
          Color(0xFF050713),
          CinematicDesign.Background,
        ),
      ),
    ),
  ) {
    Canvas(Modifier.fillMaxSize()) {
      // Large atmospheric glows keep empty areas alive without becoming a backdrop image.
      drawCircle(
        brush = Brush.radialGradient(listOf(CinematicDesign.Purple.copy(alpha = .12f), Color.Transparent)),
        radius = size.minDimension * .72f,
        center = Offset(size.width * (.08f + .08f * phase), size.height * .12f),
      )
      drawCircle(
        brush = Brush.radialGradient(listOf(CinematicDesign.Blue.copy(alpha = .10f), Color.Transparent)),
        radius = size.minDimension * .62f,
        center = Offset(size.width * (.92f - .10f * phase), size.height * .52f),
      )
      drawCircle(
        brush = Brush.radialGradient(listOf(CinematicDesign.Pink.copy(alpha = .08f), Color.Transparent)),
        radius = size.minDimension * .55f,
        center = Offset(size.width * .52f, size.height * .94f),
      )

      // Upper flowing ribbon from the reference image.
      val top = Path().apply {
        moveTo(-40f, size.height * .055f)
        cubicTo(size.width * .18f, size.height * .13f, size.width * .27f, -10f, size.width * .46f, size.height * .045f)
        cubicTo(size.width * .66f, size.height * .12f, size.width * .76f, size.height * .015f, size.width + 40f, size.height * .08f)
      }
      drawPath(
        top,
        brush = Brush.horizontalGradient(listOf(Color.Transparent, CinematicDesign.Blue.copy(glow), CinematicDesign.Purple.copy(glow), CinematicDesign.Pink.copy(glow), Color.Transparent)),
        style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round),
      )

      val lower = Path().apply {
        moveTo(-50f, size.height * .93f)
        cubicTo(size.width * .18f, size.height * .86f, size.width * .26f, size.height * .98f, size.width * .48f, size.height * .91f)
        cubicTo(size.width * .70f, size.height * .84f, size.width * .80f, size.height * .98f, size.width + 50f, size.height * .89f)
      }
      drawPath(
        lower,
        brush = Brush.horizontalGradient(listOf(Color.Transparent, CinematicDesign.Purple.copy(glow), CinematicDesign.Blue.copy(glow), CinematicDesign.Pink.copy(glow), Color.Transparent)),
        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
      )
    }

    // Soft secondary glow around the top/bottom ribbons.
    Box(
      Modifier
        .fillMaxWidth()
        .height(70.dp)
        .blur(24.dp)
        .background(
          Brush.horizontalGradient(
            listOf(Color.Transparent, CinematicDesign.Purple.copy(.09f), CinematicDesign.Blue.copy(.08f), CinematicDesign.Pink.copy(.09f), Color.Transparent),
          ),
        ),
    )

    content()
  }
}

@Composable
fun CinematicChromePanel(
  modifier: Modifier = Modifier,
  accent: Color = CinematicDesign.Purple,
  content: @Composable BoxScope.() -> Unit,
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(28.dp))
      .background(CinematicDesign.Surface.copy(alpha = .94f))
      .border(
        width = 1.dp,
        brush = Brush.linearGradient(
          listOf(accent.copy(.70f), CinematicDesign.Blue.copy(.32f), CinematicDesign.Pink.copy(.48f)),
        ),
        shape = RoundedCornerShape(28.dp),
      ),
    content = content,
  )
}
