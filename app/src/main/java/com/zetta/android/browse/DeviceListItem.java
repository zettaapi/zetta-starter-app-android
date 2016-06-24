package com.zetta.android.browse;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;

import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaStyle;

class DeviceListItem implements ListItem {

    private final ZettaDeviceId deviceId;
    private final String name;
    private final String state;
    private final ZettaStyle style;

    public DeviceListItem(ZettaDeviceId zettaDeviceId, String name, String state, ZettaStyle style) {
        deviceId = zettaDeviceId;
        this.name = name;
        this.state = state;
        this.style = style;
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
        return style.getStateImage();
    }

    @ColorInt
    public int getImageColorFilter() {
        return style.getTintColor();
    }

    public Drawable createBackground() {
        return style.createBackground();
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
