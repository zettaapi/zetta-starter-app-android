package com.zetta.android.device.actions;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;

public class ActionToggleListItem implements ListItem {

    private final ZettaDeviceId deviceId;
    private final String label;
    private final String action;
    private final ColorStateList foregroundColorList;
    private final ColorStateList backgroundColorList;
    private final Drawable foregroundDrawable;
    private final Drawable backgroundDrawable;

    public ActionToggleListItem(ZettaDeviceId deviceId,
                                String label,
                                String action,
                                ColorStateList foregroundColorList,
                                ColorStateList backgroundColorList,
                                Drawable foregroundDrawable,
                                Drawable backgroundDrawable) {
        this.deviceId = deviceId;
        this.label = label;
        this.action = action;
        this.foregroundColorList = foregroundColorList;
        this.backgroundColorList = backgroundColorList;
        this.foregroundDrawable = foregroundDrawable;
        this.backgroundDrawable = backgroundDrawable;
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
        return foregroundColorList;
    }

    public ColorStateList getActionTextColor() {
        return backgroundColorList;
    }

    public Drawable getActionBackground() {
        return foregroundDrawable;
    }

    public Drawable getBackground() {
        return backgroundDrawable;
    }
}
