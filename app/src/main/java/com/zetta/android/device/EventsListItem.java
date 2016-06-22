package com.zetta.android.device;

import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;

class EventsListItem implements ListItem {

    private final ZettaDeviceId deviceId;
    private final String description;
    private final Drawable background;
    private final int foregroundColor;

    public EventsListItem(ZettaDeviceId deviceId, String description, Drawable background, int foregroundColor) {
        this.deviceId = deviceId;
        this.description = description;
        this.background = background;
        this.foregroundColor = foregroundColor;
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

    public Drawable getBackground() {
        return background;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }
}
