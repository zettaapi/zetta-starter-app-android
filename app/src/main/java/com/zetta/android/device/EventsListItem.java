package com.zetta.android.device;

import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaStyle;

class EventsListItem implements ListItem {

    private final ZettaDeviceId deviceId;
    private final String description;
    private final ZettaStyle style;

    public EventsListItem(ZettaDeviceId deviceId, String description, ZettaStyle style) {
        this.deviceId = deviceId;
        this.description = description;
        this.style = style;
    }

    public ZettaDeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public int getType() {
        return TYPE_EVENTS;
    }

    public String getDescription() {
        return description;
    }

    public Drawable createBackground() {
        return style.createBackgroundDrawable();
    }

    public int getForegroundColor() {
        return style.getForegroundColor();
    }
}
