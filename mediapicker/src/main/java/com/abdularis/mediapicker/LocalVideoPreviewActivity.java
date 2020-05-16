package com.abdularis.mediapicker;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.abdularis.mediapicker.data.Media;

public class LocalVideoPreviewActivity extends BaseLocalPreviewActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video_preview);
        initToolbar(R.id.toolbar);

        Media media = getMediaObject();
        if (media == null || !media.isVideo()) {
            finish();
            return;
        }

        videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(media.getPath());
        videoView.start();

        MediaController controller = new MediaController(this);
        controller.setAnchorView(findViewById(R.id.root));
        controller.setMediaPlayer(videoView);

        videoView.setMediaController(controller);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoView.pause();
        }
    }
}
