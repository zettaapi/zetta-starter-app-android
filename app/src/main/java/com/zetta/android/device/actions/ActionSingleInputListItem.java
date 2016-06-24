package com.zetta.android.device.actions;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaStyle;

public class ActionSingleInputListItem implements ListItem {

    private final ZettaDeviceId deviceId;
    private final String label;
    private final String action;
    private final ZettaStyle zettaStyle;

    public ActionSingleInputListItem(ZettaDeviceId deviceId,
                                     String label,
                                     String action,
                                     ZettaStyle zettaStyle) {
        this.deviceId = deviceId;
        this.label = label;
        this.action = action;
        this.zettaStyle = zettaStyle;
    }

    public ZettaDeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public int getType() {
        return TYPE_ACTION_SINGLE_INPUT;
    }

    public String getLabel() {
        return label;
    }

    public String getAction() {
        return action;
    }

    public ColorStateList getActionTextColor() {
        return zettaStyle.getBackgroundColorList();
    }

    public ColorStateList getActionInputTextColor() {
        return zettaStyle.getForegroundColorList();
    }

    public Drawable getActionBackground() {
        return zettaStyle.createForegroundDrawable();
    }

    public Drawable getBackground() {
        return zettaStyle.createBackgroundDrawable();
    }
}
