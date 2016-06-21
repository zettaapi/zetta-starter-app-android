package com.zetta.android.device;

import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;

class PropertyListItem implements ListItem {

    private final String property;
    private final String value;
    private final Drawable backgroundDrawable;
    private final int foregroundColor;

    public PropertyListItem(String property, String value, Drawable backgroundDrawable, int foregroundColor) {
        this.property = property;
        this.value = value;
        this.backgroundDrawable = backgroundDrawable;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public int getType() {
        return TYPE_PROPERTY;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }
}
