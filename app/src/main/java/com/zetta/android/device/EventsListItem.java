package com.zetta.android.device;

import com.zetta.android.ListItem;

class EventsListItem implements ListItem {

    private final String description;

    public EventsListItem(String description) {
        this.description = description;
    }

    @Override
    public int getType() {
        return TYPE_EVENTS;
    }

    public String getDescription() {
        return description;
    }
}
