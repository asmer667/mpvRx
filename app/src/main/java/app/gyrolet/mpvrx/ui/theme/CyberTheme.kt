package app.gyrolet.mpvrx.ui.theme // عدل اسم الـ package ليطابق مشروعك إذا كان مختلفاً

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object Cyber3DColors {
    // الألوان الرئيسية للنيون والأبعاد الثلاثية
    val CyanNeon = Color(0xFF00F0FF)
    val MagentaNeon = Color(0xFFFF007F)
    val PurpleNeon = Color(0xFF8A00FF)
    val YellowCyber = Color(0xFFFFE600)
    
    // خلفيات وأسطح ثلاثية الأبعاد
    val DarkBackground = Color(0xFF090A0F)
    val DarkGlassSurface = Color(0xDD121420)
    val DarkGlassBorder = Color(0x44FFFFFF)
    
    // تدرجات حواف ثلاثية الأبعاد (3D Bevel Highlights)
    val BevelLightGradient = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.6f),
            Color.White.copy(alpha = 0.1f),
            Color.Transparent
        )
    )
    
    val BevelNeonGradient = Brush.linearGradient(
        colors = listOf(CyanNeon, MagentaNeon)
    )
    
    val CardBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1C29),
            Color(0xFF0E1017)
        )
    )
}
