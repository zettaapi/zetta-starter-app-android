package com.zetta.android.browse;

import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;

class EmptyServerListItem implements ListItem {

    private final String message;
    private final Drawable background;

    public EmptyServerListItem(String message, Drawable background) {
        this.message = message;
        this.background = background;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_EMPTY_SERVER;
    }

    public String getMessage() {
        return message;
    }

    public Drawable getBackground() {
        return background;
    }
}
