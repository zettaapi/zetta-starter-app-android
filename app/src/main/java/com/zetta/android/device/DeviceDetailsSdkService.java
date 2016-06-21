package com.zetta.android.device;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKDeviceId;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKStream;
import com.apigee.zettakit.ZIKStreamEntry;
import com.apigee.zettakit.ZIKTransition;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaSdkApi;
import com.zetta.android.ZettaStyleParser;
import com.zetta.android.device.actions.ActionListItemParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class DeviceDetailsSdkService {

    private static final Map<UUID, ZettaDeviceId> zettaDeviceIdCache = new HashMap<>();

    private final ZettaSdkApi zettaSdkApi;
    private final ZettaStyleParser zettaStyleParser;
    private final ActionListItemParser actionListItemParser;

    public DeviceDetailsSdkService() {
        zettaSdkApi = ZettaSdkApi.INSTANCE;
        zettaStyleParser = new ZettaStyleParser();
        actionListItemParser = new ActionListItemParser();
    }

    @NonNull
    public DeviceDetailsService.Device getDeviceDetails(ZettaDeviceId deviceId) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());

        final ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        final ZIKDevice zikDevice = zettaSdkApi.getLiteDevice(zikDeviceId);
        final List<ListItem> deviceListItems = convertToDeviceListItems(zikServer, zikDevice);
        return new DeviceDetailsService.Device() {
            @Override
            public String getName() {
                return zikServer.getName();
            }

            @Override
            public String getSeverName() {
                return zikDevice.getName();
            }

            @Override
            public List<ListItem> getListItems() {
                return deviceListItems;
            }
        };
    }

    @NonNull
    private List<ListItem> convertToDeviceListItems(ZIKServer zikServer, ZIKDevice zikDevice) {
        List<ListItem> listItems = new ArrayList<>();
        listItems.add(new ListItem.HeaderListItem("Actions"));

        ZettaStyleParser.Style style = zettaStyleParser.parseStyle(zikServer, zikDevice);

        List<ZIKTransition> transitions = zikDevice.getTransitions();
        if (transitions.isEmpty()) {
            listItems.add(createEmptyActionsListItem(style));
        }
        for (ZIKTransition transition : transitions) {
            listItems.add(actionListItemParser.parseActionListItem(style, transition));
        }

        listItems.add(new ListItem.HeaderListItem("Streams"));

        List<ZIKStream> allStreams = zikDevice.getAllStreams();
        for (ZIKStream stream : allStreams) {
            listItems.add(createInitialStreamListItem(style, zikDevice, stream));
        }

        listItems.add(new ListItem.HeaderListItem("Properties"));

        Map<String, Object> deviceProperties = zikDevice.getProperties();
        for (String propertyName : deviceProperties.keySet()) {
            if (propertyName.equals("style")) {
                continue;
            }
            listItems.add(createPropertyListItem(style, deviceProperties, propertyName));
        }
        if (deviceProperties.isEmpty()) {
            listItems.add(createEmptyPropertiesListItem(style));
        }

        listItems.add(new ListItem.HeaderListItem("Events"));

        listItems.add(new EventsListItem("View Events (...)"));

        return listItems;
    }

    @NonNull
    private ListItem.EmptyListItem createEmptyActionsListItem(ZettaStyleParser.Style style) {
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new ListItem.EmptyListItem("No actions for this device.", backgroundDrawable);
    }

    @NonNull
    private StreamListItem createInitialStreamListItem(ZettaStyleParser.Style style, ZIKDevice device, ZIKStream zikStream) {
        String stream = zikStream.getTitle();
        String value = "";
        ZettaDeviceId zettaDeviceId = getDeviceId(device);
        Drawable deviceBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new StreamListItem(
            zettaDeviceId,
            stream,
            value,
            style.getForegroundColor(),
            deviceBackgroundDrawable
        );
    }

    @NonNull
    private ListItem.EmptyListItem createEmptyPropertiesListItem(ZettaStyleParser.Style style) {
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new ListItem.EmptyListItem("No properties for this device.", backgroundDrawable);
    }

    @NonNull
    private PropertyListItem createPropertyListItem(ZettaStyleParser.Style style, Map<String, Object> deviceProperties, String propertyName) {
        String propertyValue = String.valueOf(deviceProperties.get(propertyName));
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new PropertyListItem(propertyName, propertyValue, backgroundDrawable, style.getForegroundColor());
    }

    public void startMonitorStreamedUpdatesFor(final ZettaDeviceId deviceId, final DeviceDetailsService.StreamListener listener) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
        ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        ZIKDevice zikDevice = zettaSdkApi.getLiteDevice(zikDeviceId);
        final ZettaStyleParser.Style style = zettaStyleParser.parseStyle(zikServer, zikDevice);
        zettaSdkApi.startMonitoringDeviceStreamsFor(zikDeviceId, new ZettaSdkApi.ZikStreamEntryListener() {
            @Override
            public void updateFor(ZIKServer server, ZIKDevice device, ZIKStreamEntry entry) {
                StreamListItem listItem = createStreamListItem(style, device, entry);
                listener.onUpdated(listItem);
            }
        });
    }

    @NonNull
    private StreamListItem createStreamListItem(ZettaStyleParser.Style style, ZIKDevice device, ZIKStreamEntry entry) {
        ZettaDeviceId zettaDeviceId = getDeviceId(device);
        String stream = entry.getTitle();
        String value = String.valueOf(entry.getData());
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(style.getBackgroundColor());
        return new StreamListItem(
            zettaDeviceId,
            stream,
            value,
            style.getForegroundColor(),
            backgroundDrawable
        );
    }

    @NonNull
    private ZettaDeviceId getDeviceId(ZIKDevice device) {
        UUID uuid = device.getDeviceId().getUuid();
        if (zettaDeviceIdCache.containsKey(uuid)) {
            return zettaDeviceIdCache.get(uuid);
        } else {
            ZettaDeviceId zettaDeviceId = new ZettaDeviceId(uuid);
            zettaDeviceIdCache.put(uuid, zettaDeviceId);
            return zettaDeviceId;
        }
    }

    public void stopMonitoringStreamedUpdates() {
        zettaSdkApi.stopMonitoringDeviceStreams();
    }
}
