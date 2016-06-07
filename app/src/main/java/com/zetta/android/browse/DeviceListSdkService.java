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
import com.apigee.zettakit.callbacks.ZIKRootCallback;
import com.apigee.zettakit.callbacks.ZIKServersCallback;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class DeviceListSdkService {

    private static final int DEFAULT_COLOR = Color.parseColor("#f2f2f2");

    public static List<ListItem> getListItems(final String url) {
        return callSdkSynchronously(url);
    }

    private static List<ListItem> callSdkSynchronously(String url) {
        final List<ListItem> items = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);

        final ZIKSession zikSession = ZIKSession.getSharedSession();
        zikSession.getRoot(url, new ZIKRootCallback() {
            @Override
            public void onSuccess(@NonNull ZIKRoot root) {
                zikSession.getServers(root, new ZIKServersCallback() {
                    @Override
                    public void onFinished(@Nullable List<ZIKServer> servers) {
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

                                    ZIKStyle deviceStyle = device.getStyle();

                                    if (deviceStyle == null) {
                                        items.add(createDefaultDeviceListItem(name, state));
                                    } else {
                                        items.add(convertToDeviceListItem(name, state, deviceStyle));
                                    }
                                }
                            }
                        }
                        latch.countDown();
                    }
                });
            }

            @Override
            public void onError(@NonNull String error) {
                Log.e("xxx", "Foobar'd in DeviceListMockService " + error);
            }
        });

        try {
            latch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // will just return an empty list
        }
        return items;
    }

    @NonNull
    private static ServerListItem createDefaultServerListItem(String serverName) {
        int serverForegroundColor = Color.parseColor("#000000");

        int serverBackgroundColor = DEFAULT_COLOR;
        Drawable serverBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(serverBackgroundColor);

        return new ServerListItem(serverForegroundColor, serverBackgroundDrawable, serverName);
    }

    @NonNull
    private static ServerListItem convertToServerListItem(String serverName, ZIKStyle serverStyle) {
        ZIKStyleColor zikForegroundColor = serverStyle.getForegroundColor();
        int serverForegroundColor;
        if (zikForegroundColor == null) {
            serverForegroundColor = DEFAULT_COLOR;
        } else {
            String jsonForegroundColor = zikForegroundColor.getHex();
            serverForegroundColor = Color.parseColor(jsonForegroundColor);
        }

        ZIKStyleColor zikBackgroundColor = serverStyle.getBackgroundColor();
        int serverBackgroundColor;
        if (zikBackgroundColor == null) {
            serverBackgroundColor = DEFAULT_COLOR;
        } else {
            String jsonBackgroundColor = zikBackgroundColor.getHex();
            serverBackgroundColor = Color.parseColor(jsonBackgroundColor);
        }
        Drawable serverBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(serverBackgroundColor);

        return new ServerListItem(serverForegroundColor, serverBackgroundDrawable, serverName);
    }

    @NonNull
    private static EmptyServerListItem createEmptyServerListItem() {
        Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_COLOR);
        return new EmptyServerListItem("No devices online for this server", backgroundDrawable);
    }

    @NonNull
    private static DeviceListItem createDefaultDeviceListItem(String name, String state) {
        int deviceForegroundColor = DEFAULT_COLOR;

        int deviceBackgroundColor = DEFAULT_COLOR;
        Drawable deviceBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceBackgroundColor);

        Uri stateImageUri = Uri.EMPTY;

        return new DeviceListItem(name, state,
                                  stateImageUri,
                                  deviceForegroundColor,
                                  deviceBackgroundDrawable
        );
    }

    @NonNull
    private static DeviceListItem convertToDeviceListItem(String name, String state, ZIKStyle deviceStyle) {
        ZIKStyleColor zikForegroundColor = deviceStyle.getForegroundColor();
        int deviceForegroundColor;
        if (zikForegroundColor == null) {
            deviceForegroundColor = DEFAULT_COLOR;
        } else {
            String jsonForegroundColor = zikForegroundColor.getHex();
            deviceForegroundColor = Color.parseColor(jsonForegroundColor);
        }

        ZIKStyleColor zikBackgroundColor = deviceStyle.getBackgroundColor();
        int deviceBackgroundColor;
        if (zikBackgroundColor == null) {
            deviceBackgroundColor = DEFAULT_COLOR;
        } else {
            String jsonBackgroundColor = zikBackgroundColor.getHex();
            deviceBackgroundColor = Color.parseColor(jsonBackgroundColor);
        }
        Drawable deviceBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceBackgroundColor);

        Uri stateImageUri;
        Map stateImage = (Map) deviceStyle.getProperties().get("stateImage");
        if (stateImage == null) {
            stateImageUri = Uri.EMPTY;
        } else {
            String jsonUrl = (String) stateImage.get("url");
            stateImageUri = Uri.parse(jsonUrl);
        }
        return new DeviceListItem(name, state,
                                  stateImageUri,
                                  deviceForegroundColor,
                                  deviceBackgroundDrawable
        );
    }

}
