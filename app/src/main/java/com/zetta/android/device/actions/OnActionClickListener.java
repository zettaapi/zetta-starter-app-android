package com.zetta.android.device.actions;

import java.util.Map;

public interface OnActionClickListener {
    void onActionClick(String label, boolean on);

    void onActionClick(String label, String input);

    void onActionClick(String label, Map<String, String> inputs);
}
