package com.zetta.android.browse;

import android.support.annotation.NonNull;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKDeviceId;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKStreamEntry;
import com.apigee.zettakit.ZIKTransition;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaSdkApi;
import com.zetta.android.ZettaStyle;
import com.zetta.android.browse.DeviceListService.StreamListener;
import com.zetta.android.device.actions.ActionListItemParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class DeviceListSdkService {

    private static final Map<UUID, ZettaDeviceId> zettaDeviceIdCache = new HashMap<>();

    private final ZettaSdkApi zettaSdkApi;
    private final ZettaStyle.Parser zettaStyleParser;
    private final ActionListItemParser actionListItemParser;

    public DeviceListSdkService() {
        zettaSdkApi = ZettaSdkApi.INSTANCE;
        zettaStyleParser = new ZettaStyle.Parser();
        actionListItemParser = new ActionListItemParser();
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
            ZettaStyle serverStyle = zettaStyleParser.parseStyle(server);
            items.add(createServerListItem(serverStyle, server));

            List<ZIKDevice> zikDevices = server.getDevices();
            if (zikDevices.isEmpty()) {
                items.add(createEmptyServerListItem(serverStyle));
            } else {
                for (ZIKDevice device : zikDevices) {
                    ZettaStyle deviceStyle = zettaStyleParser.parseStyle(server, device);
                    items.add(createDeviceListItem(deviceStyle, device));
                }
            }
        }
        return items;
    }

    @NonNull
    private ServerListItem createServerListItem(ZettaStyle style, ZIKServer zikServer) {
        String serverName = zikServer.getName();
        return new ServerListItem(serverName, style);
    }

    @NonNull
    private ListItem.EmptyListItem createEmptyServerListItem(ZettaStyle style) {
        return new ListItem.EmptyListItem("No devices online for this server", style);
    }

    @NonNull
    private DeviceListItem createDeviceListItem(ZettaStyle deviceStyle, ZIKDevice device) {
        String name = device.getName();
        String state = device.getState();
        ZettaDeviceId deviceId = getDeviceId(device);
        return new DeviceListItem(
            deviceId,
            name,
            state,
            deviceStyle
        );
    }

    @NonNull
    public List<ListItem> getQuickActions(ZettaDeviceId deviceId) {
        ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
        ZIKServer zikServer = zettaSdkApi.getServerContaining(zikDeviceId);
        ZIKDevice zikDevice = zettaSdkApi.getFullDevice(zikDeviceId);
        ZettaStyle style = zettaStyleParser.parseStyle(zikServer, zikDevice);

        List<ListItem> listItems = new ArrayList<>();
        listItems.add(createDeviceHeaderListItem(zikDevice));

        List<ZIKTransition> transitions = zikDevice.getTransitions();
        if (transitions.isEmpty()) {
            listItems.add(createEmptyQuickActionsListItem(style));
        }
        for (ZIKTransition transition : transitions) {
            listItems.add(actionListItemParser.parseActionListItem(deviceId, style, transition));
        }

        return listItems;
    }

    @NonNull
    private ListItem.HeaderListItem createDeviceHeaderListItem(ZIKDevice zikDevice) {
        return new ListItem.HeaderListItem(zikDevice.getName());
    }

    @NonNull
    private ListItem.EmptyListItem createEmptyQuickActionsListItem(ZettaStyle style) {
        return new ListItem.EmptyListItem("No actions for this device.", style);
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
        ZettaStyle deviceStyle = zettaStyleParser.parseStyle(server, device);

        return new DeviceListItem(
            zettaDeviceId,
            name,
            state,
            deviceStyle
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
