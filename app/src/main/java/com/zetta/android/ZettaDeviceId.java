package com.zetta.android;

import java.io.Serializable;
import java.util.UUID;

public class ZettaDeviceId implements Serializable {

    private final UUID uuid;

    public ZettaDeviceId(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
