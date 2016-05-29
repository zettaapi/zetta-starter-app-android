package com.zetta.android;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.net.URL;

public class ImageLoader {

    public void load(URL url, ImageView imageView) {
        Glide.with(imageView.getContext())
            .load(url.toString())
            .placeholder(R.drawable.device_placeholder)
            .crossFade()
            .error(android.R.drawable.stat_notify_error)
            .into(imageView);
        // TODO find out what is wanted to happen when it fails to load
        // i.e. what is the error image
    }

}
