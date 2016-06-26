package com.zetta.android.device;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaStyle;

class PropertyListItem implements ListItem {

    @NonNull private final String property;
    @NonNull private final String value;
    @NonNull private final ZettaStyle style;

    public PropertyListItem(@NonNull String property, @NonNull String value, @NonNull ZettaStyle style) {
        this.property = property;
        this.value = value;
        this.style = style;
    }

    @Override
    public int getType() {
        return TYPE_PROPERTY;
    }

    @NonNull
    public String getProperty() {
        return property;
    }

    @NonNull
    public String getValue() {
        return value;
    }

    @NonNull
    public Drawable createBackgroundDrawable() {
        return style.createBackgroundDrawable();
    }

    public int getForegroundColor() {
        return style.getForegroundColor();
    }
}
