package com.zetta.android.device;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaStyle;

class StateListItem implements ListItem {

    @NonNull private final String state;
    @NonNull private final ZettaStyle style;

    public StateListItem(@NonNull String state, @NonNull ZettaStyle style) {
        this.state = state;
        this.style = style;
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
        return style.getStateImage();
    }

    public int getStateColor() {
        return style.getForegroundColor();
    }

    public int getBackgroundColor() {
        return style.getBackgroundColor();
    }
}
