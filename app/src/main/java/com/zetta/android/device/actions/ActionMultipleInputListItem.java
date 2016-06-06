package com.zetta.android.device.actions;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;

import java.util.List;

public class ActionMultipleInputListItem implements ListItem {

    private final List<String> labels;
    private final String action;
    private final ColorStateList foregroundColorList;
    private final Drawable backgroundDrawable;

    public ActionMultipleInputListItem(List<String> labels,
                                       String action,
                                       ColorStateList foregroundColorList,
                                       Drawable backgroundDrawable) {
        this.labels = labels;
        this.action = action;
        this.foregroundColorList = foregroundColorList;
        this.backgroundDrawable = backgroundDrawable;
    }

    @Override
    public int getType() {
        return TYPE_ACTION_MULTIPLE_INPUT;
    }

    public List<String> getLabels() {
        return labels;
    }

    public String getAction() {
        return action;
    }

    public ColorStateList getActionTextColor() {
        return foregroundColorList;
    }

    public Drawable getActionBackground() {
        return backgroundDrawable;
    }
}
