package com.zetta.android.device.actions;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaStyle;

public class ActionToggleListItem implements ListItem {

    private final ZettaDeviceId deviceId;
    private final String label;
    private final String action;
    private final ZettaStyle style;

    public ActionToggleListItem(ZettaDeviceId deviceId,
                                String label,
                                String action,
                                ZettaStyle style) {
        this.deviceId = deviceId;
        this.label = label;
        this.action = action;
        this.style = style;
    }

    public ZettaDeviceId getDeviceId() {
        return deviceId;
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

    public ColorStateList getActionInputTextColor() {
        return style.getForegroundColorList();
    }

    public ColorStateList getActionTextColor() {
        return style.getBackgroundColorList();
    }

    public Drawable createActionBackground() {
        return style.createForegroundDrawable();
    }

    public Drawable createBackground() {
        return style.createBackgroundDrawable();
    }
}
