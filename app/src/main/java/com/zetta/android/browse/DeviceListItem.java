package com.zetta.android.browse;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

import com.zetta.android.ListItem;

import java.net.URL;

class DeviceListItem implements ListItem {

    private final String name;
    private final String state;
    private final URL stateImageUrl;
    @ColorInt
    private final int foregroundColor;
    private final Drawable background;

    public DeviceListItem(String name, String state, URL stateImageUrl, @ColorInt int foregroundColor, Drawable background) {
        this.name = name;
        this.state = state;
        this.stateImageUrl = stateImageUrl;
        this.foregroundColor = foregroundColor;
        this.background = background;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_DEVICE;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public URL getStateImageUrl() {
        return stateImageUrl;
    }

    @ColorInt
    public int getStateImageColor() {
        return foregroundColor;
    }

    public Drawable getBackground() {
        return background;
    }
}
