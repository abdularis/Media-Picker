package com.abdularis.mediapicker.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.ArrayList;
import java.util.List;

public class MediaDataSource extends PositionalDataSource<Media> {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Cursor mCursor;
    private ContentResolver mContentResolver;
    private int mMediaType;

    MediaDataSource(@NonNull ContentResolver contentResolver, int mediaType) {
        mMediaType = mediaType;
        mContentResolver = contentResolver;
        mCursor = getCursor(mediaType);
        mCursor.moveToFirst();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params,
                            @NonNull LoadInitialCallback<Media> callback) {
        int count = getCount();
        int position = computeInitialLoadPosition(params, count);
        int loadSize = computeInitialLoadSize(params, position, count);
        callback.onResult(loadData(position, loadSize), position, count);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params,
                          @NonNull LoadRangeCallback<Media> callback) {
        callback.onResult(loadData(params.startPosition, params.loadSize));
    }

    private List<Media> loadData(int position, int loadSize) {
        List<Media> mediaList = new ArrayList<>();
        if (mCursor.moveToPosition(position)) {
            int offset = position + loadSize;
            while (mCursor.getPosition() < offset && !mCursor.isAfterLast()) {

                String id = mCursor.getString(mCursor.getColumnIndex(MediaStore.MediaColumns._ID));
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                String mime = mCursor.getString(mCursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
                int width = mCursor.getInt(mCursor.getColumnIndex(MediaStore.MediaColumns.WIDTH));
                int height = mCursor.getInt(mCursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT));

                int duration = 0;
                if (mMediaType == MEDIA_TYPE_VIDEO)
                    duration = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media.DURATION));

                Media media = new Media(id, path, width, height,null, mime, duration);
                mediaList.add(media);

                mCursor.moveToNext();
            }
        }

        return mediaList;
    }

    private int getCount() {
        if (mCursor != null)
            return mCursor.getCount();
        return 0;
    }

    private Cursor getCursor(int mediaType) {
        if (mCursor != null && !mCursor.isClosed())
            mCursor.close();

        String[] projection = {
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT
        };

        if (mediaType == MEDIA_TYPE_VIDEO) {
            projection = new String[] {
                    projection[0],
                    projection[1],
                    projection[2],
                    projection[3],
                    projection[4],
                    MediaStore.Video.Media.DURATION
            };
        }

        Uri uri = mediaType == MEDIA_TYPE_IMAGE ?
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI :
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI;


        String sort = MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
        return mContentResolver.query(uri, projection, null, null, sort);
    }
}
