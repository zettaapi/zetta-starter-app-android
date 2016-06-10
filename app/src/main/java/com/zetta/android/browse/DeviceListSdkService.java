package com.zetta.android.browse;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKRoot;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKSession;
import com.apigee.zettakit.ZIKStyle;
import com.apigee.zettakit.ZIKStyleColor;
import com.apigee.zettakit.interfaces.ZIKCallback;
import com.zetta.android.BuildConfig;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.R;
import com.zetta.android.ZettaDeviceId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DeviceListSdkService {

    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#f2f2f2");
    private static final int DEFAULT_FOREGROUND_COLOR = Color.BLACK;
    private static final Uri DEFAULT_URI_ICON = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.device_placeholder);

    private int hierarchicalOneUpBackgroundColor;
    private int hierarchicalOneUpForegroundColor;

    public List<ListItem> getListItems(final String url) {
        return callSdkSynchronously(url);
    }

    private List<ListItem> callSdkSynchronously(String url) {
        final List<ListItem> items = new ArrayList<>();

        final ZIKSession zikSession = ZIKSession.getSharedSession();
        zikSession.getRootSync(url, new ZIKCallback<ZIKRoot>() {
            @Override
            public void onSuccess(@NonNull ZIKRoot root) {
                zikSession.getServersSync(root, new ZIKCallback<List<ZIKServer>>() {
                    @Override
                    public void onSuccess(@NonNull List<ZIKServer> servers) {
                        items.addAll(convertSdkTypes(servers));
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("xxx", "Foobar'd in DeviceListMockService " + exception);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("xxx", "Foobar'd in DeviceListMockService " + exception);
            }

        });

        return items;
    }

    private List<ListItem> convertSdkTypes(@Nullable List<ZIKServer> servers) {
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

}
