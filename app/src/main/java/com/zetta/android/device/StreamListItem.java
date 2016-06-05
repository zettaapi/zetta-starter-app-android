package com.zetta.android.device;

import com.zetta.android.ListItem;

class StreamListItem implements ListItem {

    private final String stream;
    private final String value;

    public StreamListItem(String stream, String value) {
        this.stream = stream;
        this.value = value;
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
}
