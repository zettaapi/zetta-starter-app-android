package com.zetta.android.browse;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;

class DeviceListItem implements ListItem {

    private final ZettaDeviceId deviceId;
    private final String name;
    private final String state;
    private final Uri stateImageUri;
    @ColorInt
    private final int foregroundColor;
    private final Drawable background;

    public DeviceListItem(ZettaDeviceId deviceId, String name, String state, Uri stateImageUri, @ColorInt int foregroundColor, Drawable background) {
        this.deviceId = deviceId;
        this.name = name;
        this.state = state;
        this.stateImageUri = stateImageUri;
        this.foregroundColor = foregroundColor;
        this.background = background;
    }

    public ZettaDeviceId getDeviceId() {
        return deviceId;
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

    public Uri getStateImageUri() {
        return stateImageUri;
    }

    @ColorInt
    public int getStateImageColor() {
        return foregroundColor;
    }

    public Drawable getBackground() {
        return background;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceListItem that = (DeviceListItem) o;

        return deviceId.equals(that.deviceId);
    }

    @Override
    public int hashCode() {
        return deviceId.hashCode();
    }
}
