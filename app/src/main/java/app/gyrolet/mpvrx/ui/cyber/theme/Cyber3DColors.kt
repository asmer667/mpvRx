/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Cyber UI - 3D Colors
 * لوحة ألوان نيونية مع تدرجات 3D للأشكال المتوهجة
 */

package app.gyrolet.mpvrx.ui.cyber.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object Cyber3DColors {
    // ═══════════════════════════════════════
    // 🎨 الألوان النيونية الأساسية
    // ═══════════════════════════════════════
    val CyanNeon = Color(0xFF00F0FF)
    val MagentaNeon = Color(0xFFFF007F)
    val PurpleNeon = Color(0xFF8A00FF)
    val YellowCyber = Color(0xFFFFE600)
    val BlueElectric = Color(0xFF0066FF)
    val PinkHot = Color(0xFFFF1E8A)

    // ═══════════════════════════════════════
    // 🌑 الخلفيات الداكنة
    // ═══════════════════════════════════════
    val BackgroundDark = Color(0xFF030108)
    val BackgroundMid = Color(0xFF0A0514)
    val BackgroundLight = Color(0xFF14082D)

    val GlassSurface = Color(0x1A14082D)
    val GlassSurfaceStrong = Color(0x330F0520)
    val DarkGlassSurface = Color(0xDD121420)
    val DarkGlassBorder = Color(0x44FFFFFF)

    // ═══════════════════════════════════════
    // ✨ تدرجات الحواف ثلاثية الأبعاد (3D Bevel)
    // ═══════════════════════════════════════
    val BevelLightGradient = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.6f),
            Color.White.copy(alpha = 0.15f),
            Color.Transparent,
        ),
    )

    val BevelNeonGradient = Brush.linearGradient(
        colors = listOf(CyanNeon, MagentaNeon),
    )

    val BevelPurpleGradient = Brush.linearGradient(
        colors = listOf(PurpleNeon, CyanNeon),
    )

    // ═══════════════════════════════════════
    // 🎴 تدرجات خلفيات البطاقات
    // ═══════════════════════════════════════
    val CardBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1C29),
            Color(0xFF0E1017),
        ),
    )

    val CardGlassGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0x22FFFFFF),
            Color(0x0AFFFFFF),
            Color(0x00FFFFFF),
        ),
    )

    val CardHighlightGradient = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.15f),
            Color.Transparent,
        ),
    )

    // ═══════════════════════════════════════
    // 💫 تدرجات التوهج (Glow)
    // ═══════════════════════════════════════
    val GlowCyan = Brush.radialGradient(
        colors = listOf(
            CyanNeon.copy(alpha = 0.4f),
            CyanNeon.copy(alpha = 0.1f),
            Color.Transparent,
        ),
    )

    val GlowMagenta = Brush.radialGradient(
        colors = listOf(
            MagentaNeon.copy(alpha = 0.4f),
            MagentaNeon.copy(alpha = 0.1f),
            Color.Transparent,
        ),
    )

    val GlowPurple = Brush.radialGradient(
        colors = listOf(
            PurpleNeon.copy(alpha = 0.35f),
            PurpleNeon.copy(alpha = 0.08f),
            Color.Transparent,
        ),
    )

    // ═══════════════════════════════════════
    // 🔥 تدرج قوسي (Sweep) للعناصر الدوارة
    // ═══════════════════════════════════════
    val SweepNeon = Brush.sweepGradient(
        colors = listOf(
            CyanNeon,
            PurpleNeon,
            MagentaNeon,
            CyanNeon,
        ),
    )

    // ═══════════════════════════════════════
    // 📝 ألوان النصوص
    // ═══════════════════════════════════════
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xCCFFFFFF)
    val TextTertiary = Color(0x88FFFFFF)
    val TextDisabled = Color(0x55FFFFFF)
}
