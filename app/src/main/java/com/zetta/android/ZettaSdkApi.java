package com.zetta.android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKDeviceId;
import com.apigee.zettakit.ZIKRoot;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKSession;
import com.apigee.zettakit.interfaces.ZIKCallback;
import com.novoda.notils.exception.DeveloperError;

import java.util.List;

/**
 * This singleton is not thread safe
 */
public enum ZettaSdkApi {
    INSTANCE;

    @Nullable
    private String rootUrl;
    @Nullable
    private ZIKRoot zikRoot;
    @Nullable
    private List<ZIKServer> zikServers;
    @Nullable
    private List<ZIKDevice> zikDevices;

    public void registerRoot(String url) {
        this.rootUrl = url;
    }

    public ZIKRoot getRoot() {
        if (rootUrl == null) {
            throw new DeveloperError("Call registerRoot(url) before getRoot!");
        }
        final ZIKSession session = ZIKSession.getSharedSession();
        session.getRootSync(rootUrl, rootZIKCallback);
        return zikRoot;
    }

    private final ZIKCallback<ZIKRoot> rootZIKCallback = new ZIKCallback<ZIKRoot>() {

        @Override
        public void onSuccess(@NonNull ZIKRoot zikRoot) {
            ZettaSdkApi.this.zikRoot = zikRoot;
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            throw new IllegalStateException(exception);
        }
    };

    public List<ZIKServer> getServers() {
        if (zikRoot == null) {
            getRoot();
        }
        ZIKSession session = ZIKSession.getSharedSession();
        session.getServersSync(zikRoot, serversZIKCallback);
        return zikServers;
    }

    private final ZIKCallback<List<ZIKServer>> serversZIKCallback = new ZIKCallback<List<ZIKServer>>() {

        @Override
        public void onSuccess(@NonNull List<ZIKServer> zikServers) {
            ZettaSdkApi.this.zikServers = zikServers;
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            throw new IllegalStateException(exception);
        }
    };

    public List<ZIKDevice> getDevices(ZIKServer zikServer) {
        if (zikServers == null) {
            throw new DeveloperError("Ensure getServers is called and completes successfully first!");
        }
        ZIKSession session = ZIKSession.getSharedSession();
        session.getDevicesSync(zikServer, devicesZIKCallback);
        return zikDevices;
    }

    private final ZIKCallback<List<ZIKDevice>> devicesZIKCallback = new ZIKCallback<List<ZIKDevice>>() {

        @Override
        public void onSuccess(@NonNull List<ZIKDevice> zikDevices) {
            ZettaSdkApi.this.zikDevices = zikDevices;
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            throw new IllegalStateException(exception);
        }
    };

    public ZIKDevice getDevice(ZIKDeviceId zikDeviceId) {
        if (zikDevices == null) {
            getDevices(getServerContaining(zikDeviceId));
        }
        for (ZIKDevice device : zikDevices) {
            if (zikDeviceId.equals(device.getDeviceId())) {
                return device;
            }
        }
        throw new DeveloperError("A device should always be found, what did you do?");
    }

    public ZIKServer getServerContaining(ZIKDeviceId zikDeviceId) {
        if (zikServers == null) {
            throw new DeveloperError("Ensure getServers is called and completes successfully first!");
        }
        for (ZIKServer zikServer : zikServers) {
            for (ZIKDevice device : zikServer.getDevices()) {
                if (zikDeviceId.equals(device.getDeviceId())) {
                    return zikServer;
                }
            }
        }
        throw new DeveloperError("A server should always be found, what did you do?");
    }

}
