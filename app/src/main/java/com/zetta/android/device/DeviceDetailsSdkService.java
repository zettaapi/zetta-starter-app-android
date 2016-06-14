package com.zetta.android.device;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKDeviceId;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKStream;
import com.apigee.zettakit.ZIKStreamEntry;
import com.apigee.zettakit.ZIKStyle;
import com.apigee.zettakit.ZIKStyleColor;
import com.apigee.zettakit.ZIKTransition;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaSdkApi;
import com.zetta.android.device.actions.ActionMultipleInputListItem;
import com.zetta.android.device.actions.ActionSingleInputListItem;
import com.zetta.android.device.actions.ActionToggleListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DeviceDetailsSdkService {

    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#f2f2f2");
    private static final int DEFAULT_FOREGROUND_COLOR = Color.BLACK;

    private int hierarchicalOneUpBackgroundColor;
    private int hierarchicalOneUpForegroundColor;

    public DeviceDetailsService.Device getDeviceDetails(ZettaDeviceId deviceId) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());

        ZettaSdkApi zettaSdkApi = ZettaSdkApi.INSTANCE;
        final ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        getHierarchicalStyleFrom(zikServer);
        final ZIKDevice zikDevice = zettaSdkApi.getDevice(zikDeviceId);
        final List<ListItem> deviceListItems = convertToDeviceListItems(zikDevice);
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

    private void getHierarchicalStyleFrom(ZIKServer server) {
        ZIKStyle serverStyle = server.getStyle();

        if (serverStyle == null) {
            hierarchicalOneUpForegroundColor = DEFAULT_FOREGROUND_COLOR;
            hierarchicalOneUpBackgroundColor = DEFAULT_BACKGROUND_COLOR;
        } else {
            ZIKStyleColor zikForegroundColor = serverStyle.getForegroundColor();
            int serverForegroundColor;
            if (zikForegroundColor == null) {
                hierarchicalOneUpForegroundColor = DEFAULT_FOREGROUND_COLOR;
            } else {
                String jsonForegroundColor = zikForegroundColor.getHex();
                serverForegroundColor = Color.parseColor(jsonForegroundColor);
                hierarchicalOneUpForegroundColor = serverForegroundColor;
            }

            ZIKStyleColor zikBackgroundColor = serverStyle.getBackgroundColor();
            int serverBackgroundColor;
            if (zikBackgroundColor == null) {
                hierarchicalOneUpBackgroundColor = DEFAULT_BACKGROUND_COLOR;
            } else {
                String jsonBackgroundColor = zikBackgroundColor.getHex();
                serverBackgroundColor = Color.parseColor(jsonBackgroundColor);
                hierarchicalOneUpBackgroundColor = serverBackgroundColor;
            }
        }
    }

    @NonNull
    private List<ListItem> convertToDeviceListItems(ZIKDevice device) {
        final List<ListItem> listItems = new ArrayList<>();
        listItems.add(new ListItem.HeaderListItem("Actions"));

        ZIKStyle deviceStyle = device.getStyle();

        int deviceForegroundColor;
        int deviceBackgroundColor;
        if (deviceStyle == null) {
            deviceForegroundColor = hierarchicalOneUpForegroundColor;
            deviceBackgroundColor = hierarchicalOneUpBackgroundColor;
        } else {
            ZIKStyleColor zikForegroundColor = deviceStyle.getForegroundColor();
            if (zikForegroundColor == null) {
                deviceForegroundColor = hierarchicalOneUpForegroundColor;
            } else {
                String jsonForegroundColor = zikForegroundColor.getHex();
                deviceForegroundColor = Color.parseColor(jsonForegroundColor);
            }

            ZIKStyleColor zikBackgroundColor = deviceStyle.getBackgroundColor();
            if (zikBackgroundColor == null) {
                deviceBackgroundColor = hierarchicalOneUpBackgroundColor;
            } else {
                String jsonBackgroundColor = zikBackgroundColor.getHex();
                deviceBackgroundColor = Color.parseColor(jsonBackgroundColor);
            }
        }

        List<ZIKTransition> transitions = device.getTransitions();
        if (transitions.isEmpty()) {
            Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceBackgroundColor);
            listItems.add(new ListItem.EmptyListItem("No actions for this device.", backgroundDrawable));
        }
        for (ZIKTransition transition : transitions) {
            listItems.add(convertToEvent(transition, deviceForegroundColor, deviceBackgroundColor));
        }

        listItems.add(new ListItem.HeaderListItem("Streams"));

        List<ZIKStream> allStreams = device.getAllStreams();
        for (ZIKStream stream : allStreams) {
            listItems.add(convertToStream(device.getDeviceId(), stream));
        }

        listItems.add(new ListItem.HeaderListItem("Properties"));

        Map<String, Object> deviceProperties = device.getProperties();
        for (String propertyName : deviceProperties.keySet()) {
            if (propertyName.equals("style")) {
                continue;
            }
            String propertyValue = String.valueOf(deviceProperties.get(propertyName));
            PropertyListItem listItem = new PropertyListItem(propertyName, propertyValue);
            listItems.add(listItem);
        }
        if (deviceProperties.isEmpty()) {
            listItems.add(new PropertyListItem("No properties for this device.", ""));
        }

        listItems.add(new ListItem.HeaderListItem("Events"));

        listItems.add(new EventsListItem("View Events (...)"));

        return listItems;
    }

    @NonNull
    private ListItem convertToEvent(ZIKTransition transition, int deviceForegroundColor, int deviceBackgroundColor) {

        List<Map<String, Object>> eventFields = transition.getFields();
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
    private StreamListItem convertToStream(ZIKDeviceId zikDeviceId, ZIKStream stream) {
        String title = stream.getTitle();
        ZettaDeviceId zettaDeviceId = new ZettaDeviceId(zikDeviceId.getUuid());
        return new StreamListItem(zettaDeviceId, title, "");
    }

    public void startMonitorStreamedUpdatesFor(ZettaDeviceId deviceId, final DeviceDetailsService.StreamListener listener) {
        ZettaSdkApi zettaSdkApi = ZettaSdkApi.INSTANCE;
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
        zettaSdkApi.startMonitoringStreamsFor(zikDeviceId, new ZettaSdkApi.ZikStreamEntryListener() {
            @Override
            public void updateFor(ZIKDeviceId deviceId, ZIKStreamEntry entry) {
                ZettaDeviceId zettaDeviceId = new ZettaDeviceId(deviceId.getUuid());
                String stream = entry.getTitle();
                String value = String.valueOf(entry.getData());
                listener.onUpdated(new StreamListItem(zettaDeviceId, stream, value));
            }
        });
    }

    public void stopMonitoringStreamedUpdates() {
        ZettaSdkApi zettaSdkApi = ZettaSdkApi.INSTANCE;
        zettaSdkApi.stopMonitoringStreams();
    }
}
