package com.zetta.android.device;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKDeviceId;
import com.apigee.zettakit.ZIKRoot;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKSession;
import com.apigee.zettakit.ZIKStyle;
import com.apigee.zettakit.ZIKStyleColor;
import com.apigee.zettakit.ZIKTransition;
import com.apigee.zettakit.callbacks.ZIKDevicesCallback;
import com.apigee.zettakit.callbacks.ZIKRootCallback;
import com.apigee.zettakit.callbacks.ZIKServersCallback;
import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.device.actions.ActionMultipleInputListItem;
import com.zetta.android.device.actions.ActionSingleInputListItem;
import com.zetta.android.device.actions.ActionToggleListItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class DeviceDetailsSdkService {

    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#f2f2f2");
    private static final int DEFAULT_FOREGROUND_COLOR = Color.BLACK;

    private int hierarchicalOneUpBackgroundColor;
    private int hierarchicalOneUpForegroundColor;

    public DeviceDetailsService.Device getDetails(String url, ZettaDeviceId deviceId) {
        return callSdkSynchronously(url, deviceId);
    }

    private DeviceDetailsService.Device callSdkSynchronously(String url, final ZettaDeviceId deviceId) {
        final DeviceDetailsService.Device[] callbackHack = new DeviceDetailsService.Device[1];
        callbackHack[0] = new DeviceDetailsService.Device() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public String getSeverName() {
                return "Error";
            }

            @Override
            public List<ListItem> getListItems() {
                return Collections.emptyList();
            }
        };

        final List<ListItem> listItems = new ArrayList<>();
        final ZIKSession zikSession = ZIKSession.getSharedSession();
        zikSession.getRootSync(url, new ZIKRootCallback() {
            @Override
            public void onSuccess(@NonNull ZIKRoot root) {
                zikSession.getServersSync(root, new ZIKServersCallback() {
                    @Override
                    public void onSuccess(@NonNull List<ZIKServer> servers) {
                        final ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());
                        for (final ZIKServer server : servers) {

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

                            zikSession.getDevicesSync(server, new ZIKDevicesCallback() {
                                @Override
                                public void onSuccess(@NonNull List<ZIKDevice> devices) {
                                    if (devices.isEmpty()) {
                                        return;
                                    }
                                    for (final ZIKDevice device : devices) {
                                        if (device.getDeviceId().equals(zikDeviceId)) {
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
                                            for (ZIKTransition transition : transitions) {
                                                List<Map<String, Object>> eventFields = transition.getFields();
                                                if (eventFields.size() == 1) {
                                                    String action = transition.getName();
                                                    ColorStateList actionTextColorList = ColorStateList.valueOf(deviceBackgroundColor);
                                                    Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceForegroundColor);
                                                    listItems.add(new ActionToggleListItem(action, action, actionTextColorList, backgroundDrawable));
                                                } else if (eventFields.size() == 2) {
                                                    Map<String, Object> eventField = eventFields.get(0);
                                                    String label = String.valueOf(eventField.get("name"));
                                                    String action = transition.getName();
                                                    ColorStateList actionTextColorList = ColorStateList.valueOf(deviceBackgroundColor);
                                                    Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceForegroundColor);
                                                    listItems.add(new ActionSingleInputListItem(label, action, actionTextColorList, backgroundDrawable));
                                                } else {
                                                    List<String> labels = new ArrayList<>();
                                                    for (Map<String, Object> eventField : eventFields) {
                                                        String label = String.valueOf(eventField.get("name"));
                                                        labels.add(label);
                                                    }
                                                    String action = transition.getName();
                                                    ColorStateList actionTextColorList = ColorStateList.valueOf(deviceBackgroundColor);
                                                    Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceForegroundColor);
                                                    listItems.add(new ActionMultipleInputListItem(labels, action, actionTextColorList, backgroundDrawable));
                                                }

                                            }
                                            if (transitions.isEmpty()) {
                                                Drawable backgroundDrawable = ImageLoader.Drawables.getBackgroundDrawableFor(deviceBackgroundColor);
                                                listItems.add(new ListItem.EmptyListItem("No actions for this device.", backgroundDrawable));
                                            }

                                            listItems.add(new ListItem.HeaderListItem("Streams"));

                                            listItems.add(new ListItem.HeaderListItem("Properties"));

                                            Map<String, Object> deviceProperties = device.getProperties();
                                            for (String propertyName : deviceProperties.keySet()) {
                                                if (propertyName.equals("style")) {
                                                    continue;
                                                }
                                                String propertyValue = String.valueOf(deviceProperties.get(propertyName));
                                                listItems.add(new PropertyListItem(propertyName, propertyValue));
                                            }
                                            if (deviceProperties.isEmpty()) {
                                                listItems.add(new PropertyListItem("No properties for this device.", ""));
                                            }

                                            listItems.add(new ListItem.HeaderListItem("Events"));

                                            callbackHack[0] = new DeviceDetailsService.Device() {
                                                @Override
                                                public String getName() {
                                                    return server.getName();
                                                }

                                                @Override
                                                public String getSeverName() {
                                                    return device.getName();
                                                }

                                                @Override
                                                public List<ListItem> getListItems() {
                                                    return listItems;
                                                }
                                            };
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull IOException exception) {
                                    Log.e(exception, "Foobar'd in DeviceListMockService");
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull IOException exception) {
                        Log.e(exception, "Foobar'd in DeviceListMockService");
                    }

                });
            }

            @Override
            public void onFailure(@NonNull IOException exception) {
                Log.e(exception, "Foobar'd in DeviceListMockService");
            }

        });

        return callbackHack[0];
    }

}
