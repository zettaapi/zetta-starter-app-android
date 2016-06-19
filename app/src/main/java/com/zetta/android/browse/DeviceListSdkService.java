package com.zetta.android.browse;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKDeviceId;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKStreamEntry;
import com.apigee.zettakit.ZIKTransition;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaSdkApi;
import com.zetta.android.ZettaStyleParser;
import com.zetta.android.browse.DeviceListService.StreamListener;
import com.zetta.android.device.actions.ActionMultipleInputListItem;
import com.zetta.android.device.actions.ActionSingleInputListItem;
import com.zetta.android.device.actions.ActionToggleListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class DeviceListSdkService {

    private static final Map<UUID, ZettaDeviceId> zettaDeviceIdCache = new HashMap<>();

    private final ZettaSdkApi zettaSdkApi;
    private final ZettaStyleParser zettaStyleParser;

    public DeviceListSdkService() {
        zettaSdkApi = ZettaSdkApi.INSTANCE;
        zettaStyleParser = new ZettaStyleParser();
    }

    @NonNull
    public List<ListItem> getListItems(String url) {
        zettaSdkApi.registerRoot(url);
        List<ZIKServer> zikServers = zettaSdkApi.getServers();
        List<ListItem> items = new ArrayList<>();
        items.addAll(createListItems(zikServers));
        return items;
    }

    @NonNull
    private List<ListItem> createListItems(List<ZIKServer> servers) {
        List<ListItem> items = new ArrayList<>();
        for (ZIKServer server : servers) {
            ZettaStyleParser.Style serverStyle = zettaStyleParser.parseStyle(server);
            items.add(createServerListItem(serverStyle, server));

            List<ZIKDevice> zikDevices = server.getDevices();
            if (zikDevices.isEmpty()) {
                items.add(createEmptyServerListItem(serverStyle));
            } else {
                for (ZIKDevice device : zikDevices) {
                    ZettaStyleParser.Style deviceStyle = zettaStyleParser.parseStyle(server, device);
                    items.add(createDeviceListItem(deviceStyle, device));
                }
            }
        }
        return items;
    }

    @NonNull
    private ServerListItem createServerListItem(ZettaStyleParser.Style serverStyle, ZIKServer zikServer) {
        String serverName = zikServer.getName();
        Drawable serverBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(serverStyle.getBackgroundColor());
        return new ServerListItem(serverStyle.getForegroundColor(), serverBackgroundDrawable, serverName);
    }

    @NonNull
    private ListItem.EmptyListItem createEmptyServerListItem(ZettaStyleParser.Style serverStyle) {
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(serverStyle.getBackgroundColor());
        return new ListItem.EmptyListItem("No devices online for this server", backgroundDrawable);
    }

    @NonNull
    private DeviceListItem createDeviceListItem(ZettaStyleParser.Style deviceStyle, ZIKDevice device) {
        String name = device.getName();
        String state = device.getState();
        ZettaDeviceId deviceId = getDeviceId(device);
        Drawable deviceBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceStyle.getBackgroundColor());
        return new DeviceListItem(
            deviceId,
            name, state,
            deviceStyle.getStateImage(),
            deviceStyle.getForegroundColor(),
            deviceBackgroundDrawable
        );
    }

    @NonNull
    public List<ListItem> getQuickActions(ZettaDeviceId deviceId) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
        ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        ZIKDevice zikDevice = zettaSdkApi.getFullDevice(zikDeviceId);
        ZettaStyleParser.Style style = zettaStyleParser.parseStyle(zikServer, zikDevice);

        List<ListItem> listItems = new ArrayList<>();
        listItems.add(createDeviceHeaderListItem(zikDevice));

        List<ZIKTransition> transitions = zikDevice.getTransitions();
        if (transitions.isEmpty()) {
            listItems.add(createEmptyQuickActionsListItem(style));
        }
        for (ZIKTransition transition : transitions) {
            listItems.add(createActionListItem(style, transition));
        }

        return listItems;
    }

    @NonNull
    private ListItem.HeaderListItem createDeviceHeaderListItem(ZIKDevice zikDevice) {
        return new ListItem.HeaderListItem(zikDevice.getName());
    }

    @NonNull
    private ListItem.EmptyListItem createEmptyQuickActionsListItem(ZettaStyleParser.Style style) {
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

    // TODO should streaming be in it's own class?
    // it shares the creation of style objects, so perhaps that should be extracted as well
    public void startMonitorStreamedUpdates(String url, final StreamListener listener) {
        zettaSdkApi.registerRoot(url);
        zettaSdkApi.startMonitoringAllServerDeviceStreams(new ZettaSdkApi.ZikStreamEntryListener() {
            @Override
            public void updateFor(ZIKServer server, ZIKDevice device, ZIKStreamEntry entry) {
                DeviceListItem listItem = createDeviceListItem(server, device, entry);
                listener.onUpdated(listItem);
            }
        });
    }

    private DeviceListItem createDeviceListItem(ZIKServer server, ZIKDevice device, ZIKStreamEntry entry) {
        ZettaDeviceId zettaDeviceId = getDeviceId(device);
        String name = device.getName();
        String state = String.valueOf(entry.getData());
        ZettaStyleParser.Style deviceStyle = zettaStyleParser.parseStyle(server, device);

        Drawable deviceBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceStyle.getBackgroundColor());
        return new DeviceListItem(
            zettaDeviceId,
            name,
            state,
            deviceStyle.getStateImage(),
            deviceStyle.getForegroundColor(),
            deviceBackgroundDrawable
        );
    }

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
        zettaSdkApi.stopMonitoringAllServerDeviceStreams();
    }

    public void reset() {
        zettaSdkApi.reset();
    }
}
