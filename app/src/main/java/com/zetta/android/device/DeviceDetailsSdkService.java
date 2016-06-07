package com.zetta.android.device;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKDeviceId;
import com.apigee.zettakit.ZIKRoot;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKSession;
import com.apigee.zettakit.callbacks.ZIKDevicesCallback;
import com.apigee.zettakit.callbacks.ZIKRootCallback;
import com.apigee.zettakit.callbacks.ZIKServersCallback;
import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class DeviceDetailsSdkService {

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
        final CountDownLatch latch = new CountDownLatch(1);

        final ZIKSession zikSession = ZIKSession.getSharedSession();
        zikSession.getRoot(url, new ZIKRootCallback() {
            @Override
            public void onSuccess(@NonNull ZIKRoot root) {
                zikSession.getServers(root, new ZIKServersCallback() {
                    @Override
                    public void onFinished(@Nullable List<ZIKServer> servers) {
                        for (final ZIKServer server : servers) {
                            zikSession.getDevices(server, new ZIKDevicesCallback() {
                                ZIKDeviceId zikDeviceId = new ZIKDeviceId(deviceId.getUuid().toString());

                                @Override
                                public void onFinished(@Nullable List<ZIKDevice> devices) {
                                    if (devices == null || devices.isEmpty()) {
                                        return;
                                    }
                                    for (final ZIKDevice device : devices) {
                                        if (device.getDeviceId().equals(zikDeviceId)) {
                                            listItems.add(new ListItem.HeaderListItem("Actions"));

                                            listItems.add(new ListItem.HeaderListItem("Streams"));

                                            listItems.add(new ListItem.HeaderListItem("Properties"));

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
                                            latch.countDown();
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onError(@NonNull String error) {
                Log.e(error, "Foobar'd in DeviceListMockService");
                latch.countDown();
            }
        });

        try {
            latch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // will just return an error item
        }

        return callbackHack[0];
    }

}
