package com.zetta.android.device.actions;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;

import java.util.List;

public class ActionMultipleInputListItem implements ListItem {

    private final List<String> labels;
    private final String action;
    private final ColorStateList backgroundColorList;
    private final ColorStateList foregroundColorList;
    private final Drawable foregroundDrawable;
    private final Drawable backgroundDrawable;

    public ActionMultipleInputListItem(List<String> labels,
                                       String action,
                                       ColorStateList backgroundColorList,
                                       ColorStateList foregroundColorList,
                                       Drawable foregroundDrawable,
                                       Drawable backgroundDrawable) {
        this.labels = labels;
        this.action = action;
        this.backgroundColorList = backgroundColorList;
        this.foregroundColorList = foregroundColorList;
        this.foregroundDrawable = foregroundDrawable;
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

    public ColorStateList getActionInputTextColor() {
        return backgroundColorList;
    }

    public Drawable getActionBackground() {
        return foregroundDrawable;
    }

    public Drawable getBackground() {
        return backgroundDrawable;
    }
}
