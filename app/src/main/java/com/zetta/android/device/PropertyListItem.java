package com.zetta.android.device;

import com.zetta.android.ListItem;

class PropertyListItem implements ListItem {

    private final String property;
    private final String value;

    public PropertyListItem(String property, String value) {
        this.property = property;
        this.value = value;
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
}
