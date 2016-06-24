package com.zetta.android.device;

import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaStyle;

class PropertyListItem implements ListItem {

    private final String property;
    private final String value;
    private final ZettaStyle style;

    public PropertyListItem(String property, String value, ZettaStyle style) {
        this.property = property;
        this.value = value;
        this.style = style;
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

    public Drawable createBackgroundDrawable() {
        return style.createBackgroundDrawable();
    }

    public int getForegroundColor() {
        return style.getForegroundColor();
    }
}
