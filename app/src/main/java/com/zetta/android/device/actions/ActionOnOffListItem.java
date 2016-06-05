package com.zetta.android.device.actions;

import android.content.res.ColorStateList;

import com.zetta.android.ListItem;

public class ActionOnOffListItem implements ListItem {

    private final String label;
    private final String action;
    private final ColorStateList foregroundColorList;
    private final ColorStateList backgroundColorList;

    public ActionOnOffListItem(String label,
                               String action,
                               ColorStateList foregroundColorList, ColorStateList backgroundColorList) {
        this.label = label;
        this.action = action;
        this.foregroundColorList = foregroundColorList;
        this.backgroundColorList = backgroundColorList;
    }

    @Override
    public int getType() {
        return TYPE_ACTION_ON_OFF;
    }

    public String getLabel() {
        return label;
    }

    public String getAction() {
        return action;
    }

    public ColorStateList getActionColorList() {
        return foregroundColorList;
    }

    public ColorStateList getActionTextColorList() {
        return backgroundColorList;
    }
}
