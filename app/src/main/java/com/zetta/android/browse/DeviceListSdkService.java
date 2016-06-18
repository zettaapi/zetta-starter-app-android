package com.zetta.android.browse;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKStreamEntry;
import com.apigee.zettakit.ZIKStyle;
import com.apigee.zettakit.ZIKStyleColor;
import com.zetta.android.BuildConfig;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.R;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaSdkApi;
import com.zetta.android.browse.DeviceListService.StreamListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DeviceListSdkService {

    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#f2f2f2");
    private static final int DEFAULT_FOREGROUND_COLOR = Color.BLACK;
    private static final Uri DEFAULT_URI_ICON = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.device_placeholder);

    private int hierarchicalOneUpBackgroundColor;
    private int hierarchicalOneUpForegroundColor;

    public List<ListItem> getListItems(String url) {
        List<ListItem> items = new ArrayList<>();

        ZettaSdkApi zettaSdkApi = ZettaSdkApi.INSTANCE;
        zettaSdkApi.registerRoot(url);
        List<ZIKServer> zikServers = zettaSdkApi.getServers();
        List<ListItem> listItemServers = convertSdkTypes(zikServers);
        items.addAll(listItemServers);

        return items;
    }

    private List<ListItem> convertSdkTypes(List<ZIKServer> servers) {
        List<ListItem> items = new ArrayList<>();
        for (ZIKServer server : servers) {
            String serverName = server.getName();
            ZIKStyle serverStyle = server.getStyle();
            if (serverStyle == null) {
                items.add(createDefaultServerListItem(serverName));
            } else {
                items.add(convertToServerListItem(serverName, serverStyle));
            }

            if (server.getDevices().isEmpty()) {
                items.add(createEmptyServerListItem());
            } else {

                for (ZIKDevice device : server.getDevices()) {
                    String name = device.getName();
                    String state = device.getState();
                    ZettaDeviceId deviceId = new ZettaDeviceId(device.getDeviceId().getUuid());
                    ZIKStyle deviceStyle = device.getStyle();

                    if (deviceStyle == null) {
                        items.add(createDefaultDeviceListItem(deviceId, name, state));
                    } else {
                        items.add(convertToDeviceListItem(deviceId, name, state, deviceStyle));
                    }
                }
            }
        }
        return items;
    }

    @NonNull
    private ServerListItem createDefaultServerListItem(String serverName) {
        int serverForegroundColor = DEFAULT_FOREGROUND_COLOR;
        hierarchicalOneUpForegroundColor = DEFAULT_FOREGROUND_COLOR;

        int serverBackgroundColor = DEFAULT_BACKGROUND_COLOR;
        hierarchicalOneUpBackgroundColor = DEFAULT_BACKGROUND_COLOR;
        Drawable serverBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(serverBackgroundColor);

        return new ServerListItem(serverForegroundColor, serverBackgroundDrawable, serverName);
    }

    @NonNull
    private ServerListItem convertToServerListItem(String serverName, ZIKStyle serverStyle) {
        ZIKStyleColor zikForegroundColor = serverStyle.getForegroundColor();
        int serverForegroundColor;
        if (zikForegroundColor == null) {
            serverForegroundColor = DEFAULT_FOREGROUND_COLOR;
            hierarchicalOneUpForegroundColor = DEFAULT_FOREGROUND_COLOR;
        } else {
            String jsonForegroundColor = zikForegroundColor.getHex();
            serverForegroundColor = Color.parseColor(jsonForegroundColor);
            hierarchicalOneUpForegroundColor = serverForegroundColor;
        }

        ZIKStyleColor zikBackgroundColor = serverStyle.getBackgroundColor();
        int serverBackgroundColor;
        if (zikBackgroundColor == null) {
            serverBackgroundColor = DEFAULT_BACKGROUND_COLOR;
            hierarchicalOneUpBackgroundColor = DEFAULT_BACKGROUND_COLOR;
        } else {
            String jsonBackgroundColor = zikBackgroundColor.getHex();
            serverBackgroundColor = Color.parseColor(jsonBackgroundColor);
            hierarchicalOneUpBackgroundColor = serverBackgroundColor;
        }
        Drawable serverBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(serverBackgroundColor);

        return new ServerListItem(serverForegroundColor, serverBackgroundDrawable, serverName);
    }

    @NonNull
    private static ListItem.EmptyListItem createEmptyServerListItem() {
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR);
        return new ListItem.EmptyListItem("No devices online for this server", backgroundDrawable);
    }

    @NonNull
    private DeviceListItem createDefaultDeviceListItem(ZettaDeviceId deviceId, String name, String state) {
        int deviceForegroundColor = hierarchicalOneUpForegroundColor;

        int deviceBackgroundColor = hierarchicalOneUpBackgroundColor;
        Drawable deviceBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceBackgroundColor);

        Uri stateImageUri = DEFAULT_URI_ICON;

        return new DeviceListItem(deviceId,
                                  name, state,
                                  stateImageUri,
                                  deviceForegroundColor,
                                  deviceBackgroundDrawable
        );
    }

    @NonNull
    private DeviceListItem convertToDeviceListItem(ZettaDeviceId deviceId, String name, String state, ZIKStyle deviceStyle) {
        ZIKStyleColor zikForegroundColor = deviceStyle.getForegroundColor();
        int deviceForegroundColor;
        if (zikForegroundColor == null) {
            deviceForegroundColor = hierarchicalOneUpForegroundColor;
        } else {
            String jsonForegroundColor = zikForegroundColor.getHex();
            deviceForegroundColor = Color.parseColor(jsonForegroundColor);
        }

        ZIKStyleColor zikBackgroundColor = deviceStyle.getBackgroundColor();
        int deviceBackgroundColor;
        if (zikBackgroundColor == null) {
            deviceBackgroundColor = hierarchicalOneUpBackgroundColor;
        } else {
            String jsonBackgroundColor = zikBackgroundColor.getHex();
            deviceBackgroundColor = Color.parseColor(jsonBackgroundColor);
        }
        Drawable deviceBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceBackgroundColor);

        Uri stateImageUri;
        Map stateImage = (Map) deviceStyle.getProperties().get("stateImage");
        if (stateImage == null) {
            stateImageUri = DEFAULT_URI_ICON;
        } else {
            String jsonUrl = (String) stateImage.get("url");
            stateImageUri = Uri.parse(jsonUrl);
        }
        return new DeviceListItem(deviceId,
                                  name, state,
                                  stateImageUri,
                                  deviceForegroundColor,
                                  deviceBackgroundDrawable
        );
    }

    // TODO should streaming be in it's own class?
    // it shares the creation of style objects, so perhaps that should be extracted as well
    public void startMonitorStreamedUpdates(String url, final StreamListener listener) {
        ZettaSdkApi zettaSdkApi = ZettaSdkApi.INSTANCE;
        zettaSdkApi.registerRoot(url);

        zettaSdkApi.startMonitoringAllServerDeviceStreams(new ZettaSdkApi.ZikStreamEntryListener() {
            @Override
            public void updateFor(ZIKServer server, ZIKDevice device, ZIKStreamEntry entry) {
                DeviceListItem listItem = createListItem(server, device, entry);
                listener.onUpdated(listItem);
            }
        });
    }

    private DeviceListItem createListItem(ZIKServer server, ZIKDevice device, ZIKStreamEntry entry) {
        ZettaDeviceId zettaDeviceId = new ZettaDeviceId(device.getDeviceId().getUuid());
        String name = device.getName();
        String state = String.valueOf(entry.getData());

        ZIKStyle serverStyle = server.getStyle();
        if (serverStyle == null) {
            hierarchicalOneUpForegroundColor = DEFAULT_FOREGROUND_COLOR;
            hierarchicalOneUpBackgroundColor = DEFAULT_BACKGROUND_COLOR;
        } else {
            ZIKStyleColor zikForegroundColor = serverStyle.getForegroundColor();
            if (zikForegroundColor == null) {
                hierarchicalOneUpForegroundColor = DEFAULT_FOREGROUND_COLOR;
            } else {
                String jsonForegroundColor = zikForegroundColor.getHex();
                hierarchicalOneUpForegroundColor = Color.parseColor(jsonForegroundColor);
            }

            ZIKStyleColor zikBackgroundColor = serverStyle.getBackgroundColor();
            if (zikBackgroundColor == null) {
                hierarchicalOneUpBackgroundColor = DEFAULT_BACKGROUND_COLOR;
            } else {
                String jsonBackgroundColor = zikBackgroundColor.getHex();
                hierarchicalOneUpBackgroundColor = Color.parseColor(jsonBackgroundColor);
            }
        }

        ZIKStyle deviceStyle = device.getStyle();
        if (deviceStyle == null) {
            int deviceForegroundColor = hierarchicalOneUpForegroundColor;

            int deviceBackgroundColor = hierarchicalOneUpBackgroundColor;
            Drawable deviceBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceBackgroundColor);

            Uri stateImageUri = DEFAULT_URI_ICON;

            return new DeviceListItem(
                zettaDeviceId,
                name,
                state,
                stateImageUri,
                deviceForegroundColor,
                deviceBackgroundDrawable
            );
        } else {
            ZIKStyleColor zikForegroundColor = deviceStyle.getForegroundColor();
            int deviceForegroundColor;
            if (zikForegroundColor == null) {
                deviceForegroundColor = hierarchicalOneUpForegroundColor;
            } else {
                String jsonForegroundColor = zikForegroundColor.getHex();
                deviceForegroundColor = Color.parseColor(jsonForegroundColor);
            }

            ZIKStyleColor zikBackgroundColor = deviceStyle.getBackgroundColor();
            int deviceBackgroundColor;
            if (zikBackgroundColor == null) {
                deviceBackgroundColor = hierarchicalOneUpBackgroundColor;
            } else {
                String jsonBackgroundColor = zikBackgroundColor.getHex();
                deviceBackgroundColor = Color.parseColor(jsonBackgroundColor);
            }
            Drawable deviceBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceBackgroundColor);

            Uri stateImageUri;
            Map stateImage = (Map) deviceStyle.getProperties().get("stateImage");
            if (stateImage == null) {
                stateImageUri = DEFAULT_URI_ICON;
            } else {
                String jsonUrl = (String) stateImage.get("url");
                stateImageUri = Uri.parse(jsonUrl);
            }

            return new DeviceListItem(
                zettaDeviceId,
                name,
                state,
                stateImageUri,
                deviceForegroundColor,
                deviceBackgroundDrawable
            );
        }
    }

    public void stopMonitoringStreamedUpdates() {
        ZettaSdkApi zettaSdkApi = ZettaSdkApi.INSTANCE;
        zettaSdkApi.stopMonitoringAllServerDeviceStreams();
    }

    public void reset() {
        ZettaSdkApi.INSTANCE.reset();
    }
}
