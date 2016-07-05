package com.zetta.android.browse;

import android.support.annotation.NonNull;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKTransition;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaStyle;
import com.zetta.android.device.actions.ActionListItemParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class DeviceList {

    static class Parser {

        private static final Map<UUID, ZettaDeviceId> zettaDeviceIdCache = new HashMap<>();

        private final ZettaStyle.Parser zettaStyleParser;
        private final ActionListItemParser actionParser;

        public Parser(ZettaStyle.Parser zettaStyleParser, ActionListItemParser actionParser) {
            this.zettaStyleParser = zettaStyleParser;
            this.actionParser = actionParser;
        }

        @NonNull
        public List<ListItem> createListItems(@NonNull List<ZIKServer> servers) {
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
        private ServerListItem createServerListItem(@NonNull ZettaStyle style, @NonNull ZIKServer zikServer) {
            String serverName = zikServer.getName();
            return new ServerListItem(serverName, style);
        }

        @NonNull
        private ListItem.EmptyListItem createEmptyServerListItem(@NonNull ZettaStyle style) {
            return new ListItem.EmptyListItem("No devices online for this server", style);
        }

        @NonNull
        private DeviceListItem createDeviceListItem(@NonNull ZettaStyle deviceStyle, @NonNull ZIKDevice device) {
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
        public DeviceListItem createDeviceListItem(@NonNull ZIKServer server,
                                                   @NonNull ZIKDevice device) {
            ZettaStyle style = zettaStyleParser.parseStyle(server, device);
            ZettaDeviceId zettaDeviceId = getDeviceId(device);
            String name = device.getName();

            Map entities = (Map) ((Map) server.getProperties().get("style")).get("entities");
            String deviceType = device.getType();
            if (entities.containsKey(deviceType)) {
                Map deviceProperties = (Map) ((Map) entities.get(deviceType)).get("properties");
                if (deviceProperties.containsKey("state")) {
                    if (((Map) deviceProperties.get("state")).get("display").equals("none")) {
                        Iterator iterator = deviceProperties.keySet().iterator();
                        iterator.next();
                        String promotedPropertyKey = (String) iterator.next();
                        String state = String.valueOf(device.getProperties().get(promotedPropertyKey));
                        return new DeviceListItem(
                            zettaDeviceId,
                            name,
                            state,
                            style
                        );
                    }
                }

            }
            String state = device.getState();
            return new DeviceListItem(
                zettaDeviceId,
                name,
                state,
                style
            );
        }

        @NonNull
        private ZettaDeviceId getDeviceId(@NonNull ZIKDevice device) {
            UUID uuid = device.getDeviceId().getUuid();
            if (zettaDeviceIdCache.containsKey(uuid)) {
                return zettaDeviceIdCache.get(uuid);
            } else {
                ZettaDeviceId zettaDeviceId = new ZettaDeviceId(uuid);
                zettaDeviceIdCache.put(uuid, zettaDeviceId);
                return zettaDeviceId;
            }
        }

        public List<ListItem> createQuickActions(@NonNull ZIKServer zikServer, @NonNull ZIKDevice zikDevice) {
            List<ListItem> listItems = new ArrayList<>();
            ZettaStyle style = zettaStyleParser.parseStyle(zikServer, zikDevice);

            listItems.add(createDeviceHeaderListItem(zikDevice));

            List<ZIKTransition> transitions = zikDevice.getTransitions();
            if (transitions.isEmpty()) {
                listItems.add(createEmptyQuickActionsListItem(style));
            }
            ZettaDeviceId deviceId = getDeviceId(zikDevice);
            for (ZIKTransition transition : transitions) {
                if (transition.getName().startsWith("_")) {
                    continue;
                }
                listItems.add(actionParser.parseActionListItem(deviceId, style, transition));
            }
            return listItems;
        }

        @NonNull
        private ListItem.HeaderListItem createDeviceHeaderListItem(@NonNull ZIKDevice zikDevice) {
            return new ListItem.HeaderListItem(zikDevice.getName());
        }

        @NonNull
        private ListItem.EmptyListItem createEmptyQuickActionsListItem(@NonNull ZettaStyle style) {
            return new ListItem.EmptyListItem("No actions for this device.", style);
        }
    }

}
