package app.gyrolet.mpvrx.ui.components // عدل اسم الـ package ليطابق مشروعك إذا كان مختلفاً

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.video.ui.theme.Cyber3DColors

enum class CyberTab { HOME, FOLDERS, SETTINGS }

@Composable
fun CyberDockBottomBar(
    selectedTab: CyberTab,
    onTabSelected: (CyberTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Cyber3DColors.DarkGlassSurface)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        Cyber3DColors.CyanNeon.copy(alpha = 0.6f),
                        Cyber3DColors.MagentaNeon.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DockItem(
                icon = Icons.Default.Home,
                label = "الرئيسية",
                isSelected = selectedTab == CyberTab.HOME,
                onClick = { onTabSelected(CyberTab.HOME) }
            )
            DockItem(
                icon = Icons.Default.Folder,
                label = "المجلدات",
                isSelected = selectedTab == CyberTab.FOLDERS,
                onClick = { onTabSelected(CyberTab.FOLDERS) }
            )
            DockItem(
                icon = Icons.Default.Settings,
                label = "الإعدادات",
                isSelected = selectedTab == CyberTab.SETTINGS,
                onClick = { onTabSelected(CyberTab.SETTINGS) }
            )
        }
    }
}

@Composable
private fun DockItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Cyber3DColors.CyanNeon else Color.White.copy(alpha = 0.5f),
        label = "iconColor"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = iconColor
        )
    }
}
