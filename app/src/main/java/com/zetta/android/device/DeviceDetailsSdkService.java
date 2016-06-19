package com.zetta.android.device;

import android.content.res.ColorStateList;
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
import com.zetta.android.device.actions.ActionMultipleInputListItem;
import com.zetta.android.device.actions.ActionSingleInputListItem;
import com.zetta.android.device.actions.ActionToggleListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class DeviceDetailsSdkService {

    private static final Map<UUID, ZettaDeviceId> zettaDeviceIdCache = new HashMap<>();

    private final ZettaSdkApi zettaSdkApi;
    private final ZettaStyleParser zettaStyleParser;

    public DeviceDetailsSdkService() {
        zettaSdkApi = ZettaSdkApi.INSTANCE;
        zettaStyleParser = new ZettaStyleParser();
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
            listItems.add(createActionListItem(style, transition));
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
            listItems.add(createPropertyListItem(deviceProperties, propertyName));
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
    private ListItem createActionListItem(ZettaStyleParser.Style style, ZIKTransition transition) {
        List<Map<String, Object>> eventFields = transition.getFields();
        int deviceForegroundColor = style.getForegroundColor();
        int deviceBackgroundColor = style.getBackgroundColor();
        if (eventFields.size() == 1) {
            String action = transition.getName();
            ColorStateList actionTextColorList = ColorStateList.valueOf(deviceBackgroundColor);
            Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceForegroundColor);
            return new ActionToggleListItem(action, action, actionTextColorList, backgroundDrawable);
        } else if (eventFields.size() == 2) {
            Map<String, Object> eventField = eventFields.get(0);
            String label = String.valueOf(eventField.get("name"));
            String action = transition.getName();
            ColorStateList actionTextColorList = ColorStateList.valueOf(deviceBackgroundColor);
            Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceForegroundColor);
            return new ActionSingleInputListItem(label, action, actionTextColorList, backgroundDrawable);
        } else {
            List<String> labels = new ArrayList<>();
            for (Map<String, Object> eventField : eventFields) {
                String label = String.valueOf(eventField.get("name"));
                labels.add(label);
            }
            String action = transition.getName();
            ColorStateList actionTextColorList = ColorStateList.valueOf(deviceBackgroundColor);
            Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceForegroundColor);
            return new ActionMultipleInputListItem(labels, action, actionTextColorList, backgroundDrawable);
        }
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
    private PropertyListItem createPropertyListItem(Map<String, Object> deviceProperties, String propertyName) {
        String propertyValue = String.valueOf(deviceProperties.get(propertyName));
        return new PropertyListItem(propertyName, propertyValue);
    }

    public void startMonitorStreamedUpdatesFor(final ZettaDeviceId deviceId, final DeviceDetailsService.StreamListener listener) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
        ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        ZIKDevice zikDevice = zettaSdkApi.getLiteDevice(zikDeviceId);
        final ZettaStyleParser.Style style = zettaStyleParser.parseStyle(zikServer, zikDevice);
        zettaSdkApi.startMonitoringDeviceStreamsFor(zikDeviceId, new ZettaSdkApi.ZikStreamEntryListener() {
            @Override
            public void updateFor(ZIKServer server, ZIKDevice device, ZIKStreamEntry entry) {
                ZettaDeviceId zettaDeviceId = DeviceDetailsSdkService.this.getDeviceId(device);
                StreamListItem listItem = createStreamListItem(style, entry, zettaDeviceId);
                listener.onUpdated(listItem);
            }
        });
    }

    @NonNull
    private StreamListItem createStreamListItem(ZettaStyleParser.Style style, ZIKStreamEntry entry, ZettaDeviceId zettaDeviceId) {
        String stream = entry.getTitle();
        String value = String.valueOf(entry.getData());
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
