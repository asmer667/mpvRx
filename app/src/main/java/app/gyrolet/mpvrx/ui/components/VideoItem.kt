package app.gyrolet.mpvrx.ui.components

import android.net.Uri

/**
 * نموذج بيانات الفيديو لنقل وتمرير معلومات الملف إلى الواجهات المضيئة (Cyber UI)
 */
data class VideoItem(
    val id: Long = 0L,
    val title: String = "",
    val path: String = "",
    val uri: Uri? = null,
    val duration: Long = 0L,
    val size: Long = 0L,
    val resolution: String = "",
    val mimeType: String = "video/*"
)
