package com.abdularis.mediapicker.data

import java.io.Serializable

data class Media(
    val mediaId: String,
    val path: String,
    val width: Int,
    val height: Int,
    val thumbPath: String? = null,
    val mimeType: String? = null,
    val duration: Int = 0
): Serializable {
    val isVideo: Boolean
        get() = mimeType?.startsWith("video/") ?: false
}
