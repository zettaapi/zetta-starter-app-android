package com.zetta.android.device;

import android.support.annotation.NonNull;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKDeviceId;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKStreamEntry;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaSdkApi;
import com.zetta.android.ZettaStyle;
import com.zetta.android.device.actions.ActionListItemParser;

class DeviceDetailsSdkService {

    @NonNull private final ZettaSdkApi zettaSdkApi;
    @NonNull private final DeviceDetails.Parser deviceParser;

    public DeviceDetailsSdkService() {
        zettaSdkApi = ZettaSdkApi.INSTANCE;
        ZettaStyle.Parser zettaStyleParser = new ZettaStyle.Parser();
        ActionListItemParser actionListItemParser = new ActionListItemParser();
        deviceParser = new DeviceDetails.Parser(zettaStyleParser, actionListItemParser);
    }

    @NonNull
    public DeviceDetails getDeviceDetails(@NonNull ZettaDeviceId deviceId) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
        ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        ZIKDevice zikDevice = zettaSdkApi.getLiteDevice(zikDeviceId);
        return deviceParser.convertToDevice(zikServer, zikDevice);
    }

    public void startMonitorStreamedUpdatesFor(@NonNull final ZettaDeviceId deviceId,
                                               @NonNull final DeviceDetailsService.StreamListener listener) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
        zettaSdkApi.startMonitoringDeviceStreamsFor(zikDeviceId, new ZettaSdkApi.ZikStreamEntryListener() {
            @Override
            public void updateFor(@NonNull ZIKServer server, @NonNull ZIKDevice device, @NonNull ZIKStreamEntry entry) {
                ZIKDevice updatedDevice = device.fetchSync();
                DeviceDetails deviceDetails = deviceParser.convertToDevice(server, updatedDevice);
                listener.onUpdated(deviceDetails.getListItems());
            }
        });
    }

    public void stopMonitoringStreamedUpdates() {
        zettaSdkApi.stopMonitoringDeviceStreams();
    }
}
