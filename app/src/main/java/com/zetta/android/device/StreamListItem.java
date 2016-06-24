package com.zetta.android.device;

import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaStyle;

class StreamListItem implements ListItem {

    private final ZettaDeviceId deviceId;
    private final String stream;
    private final String value;
    private final ZettaStyle style;

    public StreamListItem(ZettaDeviceId deviceId, String stream, String value, ZettaStyle style) {
        this.deviceId = deviceId;
        this.stream = stream;
        this.value = value;
        this.style = style;
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

    public Drawable createBackground() {
        return style.createBackgroundDrawable();
    }

    public int getForegroundColor() {
        return style.getForegroundColor();
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
