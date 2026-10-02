package app.gyrolet.mpvrx.ui.browser.cards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

data class VideoCardUiConfig(
    val showThumbnails: Boolean = true,
    val showSubtitleIndicator: Boolean = true,
    val showSizeChip: Boolean = true,
    val showResolutionChip: Boolean = true,
    val showExtensionField: Boolean = false,
    val unlimitedNameLines: Boolean = false,
    val centerGridTitles: Boolean = false,
    val useFolderNameStyle: Boolean = false,
    val allowThumbnailGeneration: Boolean = true,
    val allowThumbnailLoading: Boolean = true,
    val gridColumns: Int = 2,
    val thumbnailWidthPx: Int = 320,
    val thumbnailHeightPx: Int = 180,
)

@Composable
fun rememberVideoCardUiConfig(
    showThumbnails: Boolean = true,
    showSubtitleIndicator: Boolean = true,
    showSizeChip: Boolean = true,
    showResolutionChip: Boolean = true,
    showExtensionField: Boolean = false,
    unlimitedNameLines: Boolean = false,
    centerGridTitles: Boolean = false,
    useFolderNameStyle: Boolean = false,
    allowThumbnailGeneration: Boolean = true,
    allowThumbnailLoading: Boolean = true,
    gridColumns: Int = 2,
    thumbnailWidthPx: Int = 320,
    thumbnailHeightPx: Int = 180,
): VideoCardUiConfig = remember(
    showThumbnails,
    showSubtitleIndicator,
    showSizeChip,
    showResolutionChip,
    showExtensionField,
    unlimitedNameLines,
    centerGridTitles,
    useFolderNameStyle,
    allowThumbnailGeneration,
    allowThumbnailLoading,
    gridColumns,
    thumbnailWidthPx,
    thumbnailHeightPx,
) {
    VideoCardUiConfig(
        showThumbnails = showThumbnails,
        showSubtitleIndicator = showSubtitleIndicator,
        showSizeChip = showSizeChip,
        showResolutionChip = showResolutionChip,
        showExtensionField = showExtensionField,
        unlimitedNameLines = unlimitedNameLines,
        centerGridTitles = centerGridTitles,
        useFolderNameStyle = useFolderNameStyle,
        allowThumbnailGeneration = allowThumbnailGeneration,
        allowThumbnailLoading = allowThumbnailLoading,
        gridColumns = gridColumns,
        thumbnailWidthPx = thumbnailWidthPx,
        thumbnailHeightPx = thumbnailHeightPx,
    )
}