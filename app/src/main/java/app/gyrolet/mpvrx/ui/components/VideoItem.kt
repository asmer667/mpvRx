package app.gyrolet.mpvrx.ui.components

import android.net.Uri
import java.util.Locale

/**
 * نموذج بيانات الفيديو لنقل وتمرير معلومات الملف إلى الواجهات المضيئة (Cyber UI)
 */
data class VideoItem(
    val id: Long = 0L,
    val title: String = "",
    val path: String = "",
    val uri: Uri? = null,
    val originalUri: String = "",
    val duration: Long = 0L,
    val size: Long = 0L,
    val resolution: String = "",
    val mimeType: String = "video/*"
) {
    val durationFormatted: String
        get() {
            if (duration <= 0L) return "00:00"
            val totalSeconds = duration / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            return if (hours > 0) {
                String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format(Locale.US, "%02d:%02d", minutes, seconds)
            }
        }

    val sizeFormatted: String
        get() {
            if (size <= 0L) return "0 B"
            val kb = size / 1024.0
            val mb = kb / 1024.0
            val gb = mb / 1024.0
            return when {
                gb >= 1.0 -> String.format(Locale.US, "%.2f GB", gb)
                mb >= 1.0 -> String.format(Locale.US, "%.1f MB", mb)
                kb >= 1.0 -> String.format(Locale.US, "%.1f KB", kb)
                else -> "$size B"
            }
        }
}