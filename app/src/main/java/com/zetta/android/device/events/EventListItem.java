package com.zetta.android.device.events;

import android.support.annotation.ColorInt;

import com.zetta.android.ListItem;

public class EventListItem implements ListItem {

    private final String transition;
    private final String timestamp;
    private final int foregroundColor;
    private final int backgroundColor;

    public EventListItem(String transition, String timestamp, int foregroundColor, int backgroundColor) {
        this.transition = transition;
        this.timestamp = timestamp;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public int getType() {
        return TYPE_EVENT;
    }

    public String getTransition() {
        return transition;
    }

    public String getTimeStamp() {
        return timestamp;
    }

    @ColorInt
    public int getForegroundColor() {
        return foregroundColor;
    }

    @ColorInt
    public int getBackgroundColor() {
        return backgroundColor;
    }
}
