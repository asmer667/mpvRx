package app.gyrolet.mpvrx.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * لوحة ألوان إضافية جديدة (Cyber3DColorsExtra)
 * لا تتعارض مع Cyber3DColors الموجود في CyberTheme.kt
 */
object Cyber3DColorsExtra {
    // الألوان النيونية الأساسية
    val CyanNeon = Color(0xFF00E5FF)
    val MagentaNeon = Color(0xFFFF00AA)
    val PurpleNeon = Color(0xFF9D00FF)
    val YellowCyber = Color(0xFFFFD500)

    // خلفيات زجاجية داكنة
    val DarkGlassSurface = Color(0xCC0A0A0F)

    // تدرجات البطاقات
    val CardBackgroundGradient = Brush.linearGradient(
        listOf(
            Color(0x22FFFFFF),
            Color(0x0AFFFFFF),
        ),
    )

    val BevelLightGradient = Brush.linearGradient(
        listOf(
            Color(0x33FFFFFF),
            Color(0x0AFFFFFF),
        ),
    )

    val BevelNeonGradient = Brush.linearGradient(
        listOf(CyanNeon, MagentaNeon),
    )
}
