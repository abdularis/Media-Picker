package com.abdularis.mediapicker.data;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.abdularis.mediapicker.R;
import com.abdularis.mediapicker.commons.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.Map;

public class MediaAdapter extends PagedListAdapter<Media, MediaAdapter.ViewHolder> {

    private Map<String, Media> mSelectedMedia = new HashMap<>();
    private OnSelectionChangedListener mSelectionChangedListener;
    private OnItemClickListener mOnItemClickListener;
    private @LayoutRes int mLayoutRes;

    public interface OnSelectionChangedListener {
        void onSelectionChanged(Map<String, Media> selected, Media current, boolean select);
    }

    public interface OnItemClickListener {
        void onItemClick(Media media, boolean isSelected);
    }

    public MediaAdapter(@LayoutRes int layoutRes, OnItemClickListener listener) {
        super(new DiffCallback());
        mLayoutRes = layoutRes;
        mOnItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutRes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Media media = getItem(position);
        if (media != null)
            holder.bind(media, mSelectedMedia.containsKey(media.getMediaId()));
    }

    public Map<String, Media> getSelectedMedia() {
        return mSelectedMedia;
    }

    public void setSelectionChangedListener(OnSelectionChangedListener listener) {
        mSelectionChangedListener = listener;
    }

    public void addSelectedMedia(Media media) {
        if (!mSelectedMedia.containsKey(media.getMediaId())) {
            mSelectedMedia.put(media.getMediaId(), media);
            if (mSelectionChangedListener != null)
                mSelectionChangedListener.onSelectionChanged(mSelectedMedia, media, true);
        }
        notifyDataSetChanged();
    }

    public void removeSelectedMedia(Media media) {
        if (mSelectedMedia.containsKey(media.getMediaId())) {
            mSelectedMedia.remove(media.getMediaId());
            if (mSelectionChangedListener != null)
                mSelectionChangedListener.onSelectionChanged(mSelectedMedia, media, false);
        }
        notifyDataSetChanged();
    }

    public void removeSelectionByPath(String path) {
        for (String key : mSelectedMedia.keySet()) {
            if (TextUtils.equals(mSelectedMedia.get(key).getPath(), path)) {
                mSelectedMedia.remove(key);
                notifyDataSetChanged();
                break;
            }
        }
    }

    private boolean toggleSelect(Media media) {
        boolean selected;
        if (mSelectedMedia.containsKey(media.getMediaId())) {
            mSelectedMedia.remove(media.getMediaId());
            selected = false;
        } else {
            mSelectedMedia.put(media.getMediaId(), media);
            selected = true;
        }

        if (mSelectionChangedListener != null)
            mSelectionChangedListener.onSelectionChanged(mSelectedMedia, media, selected);
        return selected;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mCheck;
        private ImageView mImageView;
        private TextView mTextDuration;
        private View mIconPlay;
        private View mBottomShade;

        private Media mCurrentMedia;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mCheck = itemView.findViewById(R.id.check);
            mTextDuration = itemView.findViewById(R.id.textDuration);
            mIconPlay = itemView.findViewById(R.id.iconPlay);
            mBottomShade = itemView.findViewById(R.id.bottomShade);
            mCheck.setOnClickListener(v -> {
                toggleSelect(mCurrentMedia);
            });
            itemView.setOnClickListener(v -> {
                if (mCurrentMedia != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mCurrentMedia, mCheck.isChecked());
                }
            });
        }

        @SuppressLint("DefaultLocale")
        void bind(Media media, boolean isSelected) {
            mCurrentMedia = media;
            if (media.isVideo()) {
                mTextDuration.setVisibility(View.VISIBLE);
                mIconPlay.setVisibility(View.VISIBLE);
                mBottomShade.setVisibility(View.VISIBLE);
                mTextDuration.setText(Utils.formatSeconds(media.getDuration()));
            } else {
                mTextDuration.setVisibility(View.GONE);
                mIconPlay.setVisibility(View.GONE);
                mBottomShade.setVisibility(View.GONE);
            }

            String path = media.getThumbPath();
            if (path == null)
                path = media.getPath();

            mCheck.setChecked(isSelected);
            Glide.with(itemView.getContext())
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mImageView);
        }
    }

    private static class DiffCallback extends DiffUtil.ItemCallback<Media> {
        @Override
        public boolean areItemsTheSame(@NonNull Media oldItem, @NonNull Media newItem) {
            return TextUtils.equals(oldItem.getMediaId(), newItem.getMediaId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Media oldItem, @NonNull Media newItem) {
            return false;
        }
    }
}
