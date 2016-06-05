package com.zetta.android.device;

import android.net.Uri;

import com.zetta.android.ListItem;

class StateListItem implements ListItem {

    private final String state;
    private final Uri stateImageUri;
    private final int foregroundColor;

    public StateListItem(String state, Uri stateImageUri, int foregroundColor) {
        this.state = state;
        this.stateImageUri = stateImageUri;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public int getType() {
        return TYPE_STATE;
    }

    public String getState() {
        return state;
    }

    public Uri getStateImageUri() {
        return stateImageUri;
    }

    public int getStateColor() {
        return foregroundColor;
    }
}
