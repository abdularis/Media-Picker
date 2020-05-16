package com.abdularis.mediapicker.data

import android.content.ContentResolver
import androidx.paging.DataSource

class MediaDataSourceFactory(
    private val contentResolver: ContentResolver,
    private val mediaType: Int
) : DataSource.Factory<Int, Media>() {

    override fun create(): DataSource<Int, Media> {
        return MediaDataSource(contentResolver, mediaType)
    }

}