package com.abdularis.mediapicker.data

import android.content.ContentResolver
import android.provider.MediaStore
import android.util.Log

data class Bucket(
    val bucketId: Int,
    val bucketName: String,
    var itemCount: Int
)

class MediaBucket(
    private val contentResolver: ContentResolver
) {
    suspend fun getMediaBucketList(): Collection<Bucket> {
        Log.d("TestMe", "thread = ${Thread.currentThread().name}")
        val projection = arrayOf(
            MediaStore.MediaColumns.BUCKET_ID,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME
        )

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(uri, projection, null, null, null)

        val bucketList = HashMap<Int, Bucket>()
        cursor?.apply {
            while (moveToNext()) {
                val bucketId = getInt(getColumnIndex(MediaStore.MediaColumns.BUCKET_ID))
                val bucketName = getString(getColumnIndex(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME))

                val bucket = bucketList[bucketId]
                if (bucket == null) {
                    bucketList[bucketId] = Bucket(bucketId, bucketName, 1)
                } else {
                    bucket.itemCount++
                }
            }

            close()
        }

        return bucketList.values
    }
}