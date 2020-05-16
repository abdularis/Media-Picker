package com.abdularis.mediapicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.abdularis.mediapicker.data.*
import kotlinx.android.synthetic.main.activity_media_picker.*
import java.util.Locale

class MediaPickerActivity : AppCompatActivity() {
    private lateinit var mAdapter: MediaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_picker)

        showProgress()
        btnSelect.isEnabled = false
        textSelectCount.text = ""
        btnSelect.setOnClickListener {
            onSelectClick()
        }

        val mediaClickListener = MediaAdapter.OnItemClickListener { media: Media, isSelected: Boolean ->
            onMediaItemClick(media, isSelected)
        }
        mAdapter = MediaAdapter(R.layout.item_media, mediaClickListener)
        mAdapter.setSelectionChangedListener { selection: Map<String, Media>, media: Media, select: Boolean ->
            onSelectionChanged(selection, media, select)
        }

        MediaStorePagedListBuilder.build(contentResolver, MediaDataSource.MEDIA_TYPE_IMAGE)
            .observe(this, Observer {
                mAdapter.submitList(it)
                showContent()
            })

        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun showContent() {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun onMediaItemClick(media: Media, isSelected: Boolean) {
        val intent =
            if (media.isVideo) Intent(this, LocalVideoPreviewActivity::class.java)
            else Intent(this, LocalImagePreviewActivity::class.java)

        intent.putExtra(BaseLocalPreviewActivity.EXTRA_IS_SELECTED_INITIAL, isSelected)
        intent.putExtra(BaseLocalPreviewActivity.EXTRA_MEDIA_OBJECT, media)
        startActivity(intent)
    }

    private fun onSelectClick() {
        val mediaList = mAdapter.selectedMedia.map { it.value }
        val data = Intent()
        data.putExtra(EXTRA_SELECTED_ITEMS, SelectedMedia(mediaList))
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun onSelectionChanged(selection: Map<String, Media>, media: Media, select: Boolean) {
        if (selection.isEmpty()) {
            textSelectCount.text = ""
            btnSelect.isEnabled = false
        } else {
            textSelectCount.text = String.format(Locale.getDefault(), "%d Selected", selection.size)
            btnSelect.isEnabled = true
        }
    }

    companion object {
        const val EXTRA_SELECTED_ITEMS = "res_selected"

        @JvmStatic
        fun getSelectedMediaFromIntent(intent: Intent?): List<Media>? {
            if (intent != null && intent.hasExtra(EXTRA_SELECTED_ITEMS)) {
                val data =
                    intent.getSerializableExtra(EXTRA_SELECTED_ITEMS)
                return try {
                    val selectedMedia = data as SelectedMedia
                    selectedMedia.mediaList
                } catch (e: Exception) {
                    null
                }
            }
            return null
        }
    }
}