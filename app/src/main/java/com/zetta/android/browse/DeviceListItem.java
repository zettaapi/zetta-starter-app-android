package com.zetta.android.browse;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaStyle;

class DeviceListItem implements ListItem {

    @NonNull private final ZettaDeviceId deviceId;
    @NonNull private final String name;
    @NonNull private final String state;
    @NonNull private final ZettaStyle style;

    public DeviceListItem(@NonNull ZettaDeviceId zettaDeviceId,
                          @NonNull String name,
                          @NonNull String state,
                          @NonNull ZettaStyle style) {
        deviceId = zettaDeviceId;
        this.name = name;
        this.state = state;
        this.style = style;
    }

    @NonNull
    public ZettaDeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_DEVICE;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getState() {
        return state;
    }

    @NonNull
    public Uri getStateImageUri() {
        return style.getStateImage();
    }

    @ColorInt
    public int getImageColorFilter() {
        return style.getTintColor();
    }

    @NonNull
    public Drawable createBackground() {
        return style.createBackgroundDrawable();
    }

    @ColorInt
    public int getTextColor() {
        return style.getForegroundColor();
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
