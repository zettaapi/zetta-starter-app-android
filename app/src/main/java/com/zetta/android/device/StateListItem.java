package com.zetta.android.device;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.zetta.android.ListItem;

class StateListItem implements ListItem {

    @NonNull private final String state;
    @NonNull private final Uri stateImageUri;
    private final int foregroundColor;

    public StateListItem(@NonNull String state, @NonNull Uri stateImageUri, int foregroundColor) {
        this.state = state;
        this.stateImageUri = stateImageUri;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public int getType() {
        return TYPE_STATE;
    }

    @NonNull
    public String getState() {
        return state;
    }

    @NonNull
    public Uri getStateImageUri() {
        return stateImageUri;
    }

    public int getStateColor() {
        return foregroundColor;
    }
}
