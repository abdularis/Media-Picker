package com.abdularis.mediapicker.data

import android.content.ContentResolver
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

object MediaStorePagedListBuilder {
    @JvmStatic
    fun build(contentResolver: ContentResolver, mediaType: Int): LiveData<PagedList<Media>> {
        val factory = MediaDataSourceFactory(contentResolver, mediaType)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(20)
            .setPrefetchDistance(5)
            .setPageSize(16)
            .build()
        return LivePagedListBuilder(factory, config).build()
    }
}