package com.zetta.android;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageLoader {

    public void load(Uri uri, ImageView imageView) {
        Glide.with(imageView.getContext())
            .load(uri)
            .placeholder(R.drawable.device_placeholder)
            .crossFade()
            .error(android.R.drawable.stat_notify_error)
            .into(imageView);
        // TODO find out what is wanted to happen when it fails to load
        // i.e. what is the error image
    }

}
