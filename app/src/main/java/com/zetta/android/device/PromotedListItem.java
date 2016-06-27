package com.zetta.android.device;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaStyle;

class PromotedListItem implements ListItem {

    @NonNull private final String property;
    @NonNull private final String value;
    @NonNull private final String symbol;
    @NonNull private final ZettaStyle style;

    public PromotedListItem(@NonNull String property,
                            @NonNull String value,
                            @NonNull String symbol,
                            @NonNull ZettaStyle style) {
        this.property = property;
        this.value = value;
        this.symbol = symbol;
        this.style = style;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_PROMOTED;
    }

    @NonNull
    public String getProperty() {
        return property;
    }

    @ColorInt
    public int getForegroundColor() {
        return style.getForegroundColor();
    }

    @NonNull
    public String getValue() {
        return value;
    }

    @NonNull
    public String getSymbol() {
        return symbol;
    }

    @ColorInt
    public int getBackgroundColor() {
        return style.getBackgroundColor();
    }
}
