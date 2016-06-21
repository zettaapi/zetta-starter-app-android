package com.zetta.android.device;

import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;

class EventsListItem implements ListItem {

    private final String description;
    private final Drawable background;
    private final int foregroundColor;

    public EventsListItem(String description, Drawable background, int foregroundColor) {
        this.description = description;
        this.background = background;
        this.foregroundColor = foregroundColor;
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
