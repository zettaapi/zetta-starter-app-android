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
import com.apigee.zettakit.callbacks.ZIKRootCallback;
import com.apigee.zettakit.callbacks.ZIKServersCallback;
import com.fasterxml.jackson.databind.JsonNode;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class DeviceListSdkService {

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
                        int defaultColor = Color.parseColor("#f2f2f2");
                        for (ZIKServer server : servers) {
                            String serverName = server.getName();
//                            Map<String, JsonNode> serverProperties = server.getProperties();
//                            JsonNode serverStyleProperties = serverProperties.get("style").get("properties");
                            int foregroundColor = Color.parseColor("#0000ff"); //Color.parseColor(serverStyleProperties.get("foregroundColor").get("hex").asText());
                            Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor);
                            items.add(new ServerListItem(foregroundColor, backgroundDrawable, serverName));

                            if (server.getDevices().isEmpty()) {
                                items.add(new EmptyServerListItem("No devices online for this server", backgroundDrawable));
                            } else {

                                for (ZIKDevice device : server.getDevices()) {
                                    String name = device.getName();
                                    String state = device.getState();

                                    Map<String, JsonNode> deviceProperties = device.getProperties();
                                    JsonNode deviceStyle = deviceProperties.get("style");
                                    String jsonForegroundColor = "#ff0000";// deviceStyle.get("foregroundColor").get("hex").asText();
                                    int deviceForegroundColor = Color.parseColor(jsonForegroundColor);
                                    String jsonBackgroundColor = "#ffffff"; //deviceStyle.get("backgroundColor").get("hex").asText();
                                    int deviceBackgroundColor = Color.parseColor(jsonBackgroundColor);
                                    Drawable deviceBackgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceBackgroundColor);

                                    Uri stateImageUrl;
                                    if (deviceStyle == null) {
                                        stateImageUrl = Uri.EMPTY;
                                    } else {
                                        String jsonUrl = deviceStyle.get("properties").get("stateImage").get("url").asText();
                                        stateImageUrl = Uri.parse(jsonUrl);
                                    }
                                    items.add(new DeviceListItem(name, state,
                                                                 stateImageUrl,
                                                                 deviceForegroundColor,
                                                                 deviceBackgroundDrawable
                                    ));
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

}
