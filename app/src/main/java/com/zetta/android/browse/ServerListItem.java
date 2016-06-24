package com.zetta.android.browse;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaStyle;

class ServerListItem implements ListItem {

    @ColorInt
    private final String name;
    private final ZettaStyle style;

    public ServerListItem(String name, ZettaStyle style) {
        this.name = name;
        this.style = style;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_SERVER;
    }

    @ColorInt
    public int getSwatchColor() {
        return style.getForegroundColor();
    }

    public String getName() {
        return name;
    }

    public Drawable createBackground() {
        return style.createBackgroundDrawable();
    }
}
