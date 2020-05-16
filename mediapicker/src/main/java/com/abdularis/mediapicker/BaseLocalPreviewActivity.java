package com.abdularis.mediapicker;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.abdularis.mediapicker.data.Media;

public class BaseLocalPreviewActivity extends AppCompatActivity {

    public static final String DEFAULT_TITLE = "Preview";

    public static final String EXTRA_MEDIA_OBJECT = "ext_media_obj";
    public static final String EXTRA_TITLE = "ext_title";
    public static final String EXTRA_PATH = "ext_path";
    public static final String EXTRA_IS_SELECTED_INITIAL = "ext_is_selected";
    public static final String EXTRA_IS_SELECTED_RESULT = "ext_is_selected";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initToolbar(@IdRes int toolbarId) {
        Toolbar toolbar = findViewById(toolbarId);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(getExtraTitle());
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    protected String getExtraPath() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getString(EXTRA_PATH, null);
        }
        return null;
    }

    protected String getExtraTitle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getString(EXTRA_TITLE, DEFAULT_TITLE);
        }
        return DEFAULT_TITLE;
    }

    protected boolean getIsSelectedInitial() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getBoolean(EXTRA_IS_SELECTED_INITIAL, false);
        }
        return false;
    }

    protected Media getMediaObject() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return (Media) extras.getSerializable(EXTRA_MEDIA_OBJECT);
        }
        return null;
    }
}
