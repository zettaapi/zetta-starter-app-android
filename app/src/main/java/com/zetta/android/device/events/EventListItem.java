package com.zetta.android.device.events;

import com.zetta.android.ListItem;

public class EventListItem implements ListItem {

    private final String transition;
    private final String timestamp;

    public EventListItem(String transition, String timestamp) {
        this.transition = transition;
        this.timestamp = timestamp;
    }

    public String getTransition() {
        return transition;
    }

    public String getTimeStamp() {
        return timestamp;
    }

    @Override
    public int getType() {
        return TYPE_EVENT;
    }
}
