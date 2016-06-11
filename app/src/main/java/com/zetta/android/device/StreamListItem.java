package com.zetta.android.device;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;

class StreamListItem implements ListItem {

    private final ZettaDeviceId deviceId;
    private final String stream;
    private final String value;

    public StreamListItem(ZettaDeviceId deviceId, String stream, String value) {
        this.deviceId = deviceId;
        this.stream = stream;
        this.value = value;
    }

    @Override
    public int getType() {
        return TYPE_STREAM;
    }

    public String getStream() {
        return stream;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StreamListItem)) {
            return false;
        }

        StreamListItem that = (StreamListItem) o;

        return deviceId.equals(that.deviceId)
            && stream.equals(that.stream);
    }

    @Override
    public int hashCode() {
        int result = deviceId.hashCode();
        result = 31 * result + stream.hashCode();
        return result;
    }
}
