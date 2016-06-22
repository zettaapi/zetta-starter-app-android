package com.zetta.android.device.actions;

import com.zetta.android.ZettaDeviceId;

import java.util.Map;

public interface OnActionClickListener {

    void onActionClick(ZettaDeviceId deviceId, String label, String input);

    void onActionClick(ZettaDeviceId deviceId, String label, Map<String, String> inputs);
}
