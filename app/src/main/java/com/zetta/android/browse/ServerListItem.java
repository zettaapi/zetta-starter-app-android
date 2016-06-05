package com.zetta.android.browse;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

import com.zetta.android.ListItem;

class ServerListItem implements ListItem {

    @ColorInt
    private final int swatchColor;
    private final Drawable background;
    private final String name;

    public ServerListItem(@ColorInt int foregroundColor, Drawable background, String name) {
        this.swatchColor = foregroundColor;
        this.background = background;
        this.name = name;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_SERVER;
    }

    @ColorInt
    public int getSwatchColor() {
        return swatchColor;
    }

    public String getName() {
        return name;
    }

    public Drawable getBackground() {
        return background;
    }
}
