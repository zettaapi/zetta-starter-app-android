package com.zetta.android.device.events;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKDeviceId;
import com.apigee.zettakit.ZIKLogStreamEntry;
import com.apigee.zettakit.ZIKServer;
import com.zetta.android.ImageLoader;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaSdkApi;
import com.zetta.android.ZettaStyle;

import java.util.Date;

class EventsSdkService {

    private final ZettaSdkApi zettaSdkApi;
    private final ZettaStyle.Parser zettaStyleParser;

    public EventsSdkService() {
        zettaSdkApi = ZettaSdkApi.INSTANCE;
        zettaStyleParser = new ZettaStyle.Parser();
    }

    public void startMonitorLogUpdatesFor(final ZettaDeviceId deviceId, final EventsService.StreamListener listener) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
        ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        ZIKDevice zikDevice = zettaSdkApi.getLiteDevice(zikDeviceId);
        final ZettaStyle style = zettaStyleParser.parseStyle(zikServer, zikDevice);
        zettaSdkApi.startMonitoringLogStreamFor(zikDeviceId, new ZettaSdkApi.ZikLogStreamEntryListener() {
            @Override
            public void updateFor(ZIKServer server, ZIKDevice device, ZIKLogStreamEntry entry) {
                EventListItem listItem = createEventListItem(style, device, entry);
                listener.onUpdated(listItem);
            }
        });
    }

    @NonNull
    private EventListItem createEventListItem(ZettaStyle style, ZIKDevice device, ZIKLogStreamEntry entry) {
        String transition = entry.getTransition();
        String timestamp = new Date(entry.getTimeStamp()).toString();
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new EventListItem(
            transition,
            timestamp
//            style.getForegroundColor(),
//            backgroundDrawable
        );
    }

    public void stopMonitoringStreamedUpdates() {
        zettaSdkApi.stopMonitoringLogStream      ();
    }
}
