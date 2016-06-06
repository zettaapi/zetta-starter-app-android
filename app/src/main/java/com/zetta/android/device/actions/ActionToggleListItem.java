package com.zetta.android.device.actions;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;

public class ActionToggleListItem implements ListItem {

    private final String label;
    private final String action;
    private final ColorStateList actionTextColorList;
    private final Drawable backgroundDrawable;

    public ActionToggleListItem(String label,
                                String action,
                                ColorStateList actionTextColorList,
                                Drawable backgroundDrawable) {
        this.label = label;
        this.action = action;
        this.actionTextColorList = actionTextColorList;
        this.backgroundDrawable = backgroundDrawable;
    }

    @Override
    public int getType() {
        return TYPE_ACTION_TOGGLE;
    }

    public String getLabel() {
        return label;
    }

    public String getAction() {
        return action;
    }

    public ColorStateList getActionTextColor() {
        return actionTextColorList;
    }

    public Drawable getActionBackground() {
        return backgroundDrawable;
    }
}
