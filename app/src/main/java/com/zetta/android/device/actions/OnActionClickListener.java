package com.zetta.android.device.actions;

import android.support.annotation.NonNull;

import com.zetta.android.ZettaDeviceId;

import java.util.Map;

public interface OnActionClickListener {

    void onActionClick(@NonNull ZettaDeviceId deviceId, @NonNull String label, @NonNull String input);

    void onActionClick(@NonNull ZettaDeviceId deviceId, @NonNull String label, @NonNull Map<String, String> inputs);
}
