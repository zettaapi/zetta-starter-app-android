package com.zetta.android.device;

import com.zetta.android.ListItem;

import java.net.URL;

class StateListItem implements ListItem {

    private final String state;
    private final URL stateImageUrl;
    private final int foregroundColor;

    public StateListItem(String state, URL stateImageUrl, int foregroundColor) {
        this.state = state;
        this.stateImageUrl = stateImageUrl;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public int getType() {
        return TYPE_STATE;
    }

    public String getState() {
        return state;
    }

    public URL getStateImageUrl() {
        return stateImageUrl;
    }

    public int getStateColor() {
        return foregroundColor;
    }
}
