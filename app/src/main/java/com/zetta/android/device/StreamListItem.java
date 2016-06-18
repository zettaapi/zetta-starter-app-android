package com.zetta.android.device;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;

class StreamListItem implements ListItem {

    private final ZettaDeviceId deviceId;
    private final String stream;
    private final String value;
    @ColorInt
    private final int foregroundColor;
    private final Drawable background;

    public StreamListItem(ZettaDeviceId deviceId, String stream, String value, int foregroundColor, Drawable background) {
        this.deviceId = deviceId;
        this.stream = stream;
        this.value = value;
        this.foregroundColor = foregroundColor;
        this.background = background;
    }

    @Override
    public int getType() {
        return TYPE_STREAM;
    }

    public String getStream() {
        return stream;
    }

    public String getValue() {
        return value;
    }

    public Drawable getBackground() {
        return background;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StreamListItem)) {
            return false;
        }

        StreamListItem that = (StreamListItem) o;

        return deviceId.equals(that.deviceId)
            && stream.equals(that.stream);
    }

    @Override
    public int hashCode() {
        int result = deviceId.hashCode();
        result = 31 * result + stream.hashCode();
        return result;
    }
}
