package com.zetta.android.device.actions;

import android.content.res.ColorStateList;

import com.zetta.android.ListItem;

import java.util.List;

public class ActionMultipleInputListItem implements ListItem {

    private final List<String> labels;
    private final String action;
    private final ColorStateList foregroundColorList;
    private final ColorStateList backgroundColorList;

    public ActionMultipleInputListItem(List<String> labels,
                                       String action,
                                       ColorStateList foregroundColorList, ColorStateList backgroundColorList) {
        this.labels = labels;
        this.action = action;
        this.foregroundColorList = foregroundColorList;
        this.backgroundColorList = backgroundColorList;
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

    public ColorStateList getActionColorList() {
        return foregroundColorList;
    }

    public ColorStateList getActionTextColorList() {
        return backgroundColorList;
    }
}
