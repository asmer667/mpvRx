package app.gyrolet.mpvrx.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.gyrolet.mpvrx.ui.screens.DashboardScreen
import kotlinx.serialization.Serializable

@Serializable
data object Dashboard : Screen {
    @Composable
    override fun Content() {
        DashboardScreen(
            featuredVideo = null,
            recentVideos = emptyList(),
            getThumbnail = { null },
            onVideoClick = { },
            modifier = Modifier,
        )
    }
}