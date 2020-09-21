package com.abdularis.mediapicker.data

import android.net.Uri
import java.io.Serializable

data class Media(
    val mediaId: String,
    val path: String,
    val width: Int,
    val height: Int,
    val thumbPath: String? = null,
    val mimeType: String? = null,
    val duration: Int = 0,
    val uri: Uri
): Serializable {
    val isVideo: Boolean
        get() = mimeType?.startsWith("video/") ?: false
}
