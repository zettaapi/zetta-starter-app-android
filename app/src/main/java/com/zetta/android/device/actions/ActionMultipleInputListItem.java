package com.zetta.android.device.actions;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaStyle;

import java.util.List;

public class ActionMultipleInputListItem implements ListItem {

    private final ZettaDeviceId deviceId;
    private final List<String> labels;
    private final String action;
    private final ZettaStyle style;

    public ActionMultipleInputListItem(ZettaDeviceId deviceId, List<String> labels, String action, ZettaStyle style) {
        this.deviceId = deviceId;
        this.labels = labels;
        this.action = action;
        this.style = style;
    }

    public ZettaDeviceId getDeviceId() {
        return deviceId;
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
        return style.getBackgroundColorList();
    }

    public ColorStateList getActionInputTextColor() {
        return style.getForegroundColorList();
    }

    public Drawable createActionBackground() {
        return style.createForegroundDrawable();
    }

    public Drawable createBackground() {
        return style.createBackgroundDrawable();
    }
}
