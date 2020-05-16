package com.abdularis.mediapicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.abdularis.mediapicker.data.Media;
import com.bumptech.glide.Glide;

public class LocalImagePreviewActivity extends BaseLocalPreviewActivity {

    private CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_image_preview);
        initToolbar(R.id.toolbar);

        Media media = getMediaObject();
        if (media == null) {
            finish();
            return;
        }

        ImageView imageView = findViewById(R.id.photoView);
        Glide.with(this)
                .load(media.getPath())
                .into(imageView);

        mCheckBox = findViewById(R.id.check);
        mCheckBox.setChecked(getIsSelectedInitial());
    }

    @Override
    public void onBackPressed() {
        if (getIsSelectedInitial() != mCheckBox.isChecked()) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_IS_SELECTED_RESULT, mCheckBox.isChecked());
            intent.putExtra(EXTRA_MEDIA_OBJECT, getMediaObject());
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
