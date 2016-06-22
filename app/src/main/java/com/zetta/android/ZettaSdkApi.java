package com.zetta.android;

import android.support.annotation.Nullable;

import com.apigee.zettakit.ZIKDevice;
import com.apigee.zettakit.ZIKDeviceId;
import com.apigee.zettakit.ZIKException;
import com.apigee.zettakit.ZIKLogStreamEntry;
import com.apigee.zettakit.ZIKRoot;
import com.apigee.zettakit.ZIKServer;
import com.apigee.zettakit.ZIKSession;
import com.apigee.zettakit.ZIKStream;
import com.apigee.zettakit.ZIKStreamEntry;
import com.apigee.zettakit.interfaces.ZIKStreamListener;
import com.novoda.notils.exception.DeveloperError;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * This singleton is not thread safe
 */
public enum ZettaSdkApi {
    INSTANCE;

    private final List<ZIKStream> serverDevicesStreams = new ArrayList<>();
    private final List<ZIKStream> deviceStreams = new ArrayList<>();

    @Nullable
    private String rootUrl;
    @Nullable
    private ZIKRoot zikRoot;
    @Nullable
    private List<ZIKServer> zikServers;
    @Nullable
    private List<ZIKDevice> zikDevices;
    @Nullable
    private ZIKStream logStream;

    private boolean cancelMonitoringSetup;

    public void registerRoot(String url) {
        if (url.equals(this.rootUrl)) {
            // No need to re-register
            return;
        }
        this.rootUrl = url;
        getRoot();
    }

    public ZIKRoot getRoot() {
        if (rootUrl == null) {
            throw new DeveloperError("Call registerRoot(url) before getRoot!");
        }
        final ZIKSession session = ZIKSession.getSharedSession();
        this.zikRoot = session.getRootSync(rootUrl);
        return zikRoot;
    }

    public List<ZIKServer> getServers() {
        if (zikRoot == null || !zikRoot.getSelfLink().getHref().equals(rootUrl)) {
            getRoot();
        }
        ZIKSession session = ZIKSession.getSharedSession();
        this.zikServers = session.getServersSync(zikRoot);
        return zikServers;
    }

    public List<ZIKDevice> getDevices(ZIKServer zikServer) {
        if (zikServers == null) {
            throw new DeveloperError("Ensure getServers is called and completes successfully first!");
        }
        ZIKSession session = ZIKSession.getSharedSession();
        this.zikDevices = session.getDevicesSync(zikServer);
        return zikDevices;
    }

    public ZIKDevice getLiteDevice(ZIKDeviceId zikDeviceId) {
        if (zikDevices == null) {
            getDevices(getServerContaining(zikDeviceId));
        }
        for (ZIKDevice device : zikDevices) {
            if (zikDeviceId.equals(device.getDeviceId())) {
                return device;
            }
        }
        getDevices(getServerContaining(zikDeviceId));
        for (ZIKDevice device : zikDevices) {
            if (zikDeviceId.equals(device.getDeviceId())) {
                return device;
            }
        }
        throw new DeveloperError("A device should always be found, what did you do?");
    }

    public ZIKDevice getFullDevice(ZIKDeviceId zikDeviceId) {
        return getLiteDevice(zikDeviceId).fetchSync();
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

    public void startMonitoringDeviceStreamsFor(ZIKDeviceId deviceId, ZikStreamEntryListener listener) {
        stopMonitoringDeviceStreams();
        cancelMonitoringSetup = false;
        ZIKDevice device = getLiteDevice(deviceId);
        monitorNonLogStreams(device, listener, deviceStreams);
    }

    public void stopMonitoringDeviceStreams() {
        cancelMonitoringSetup = true;
        for (ZIKStream stream : deviceStreams) {
            stream.close();
        }
        deviceStreams.clear();
    }

    public void startMonitoringAllServerDeviceStreams(final ZikStreamEntryListener listener) {
        if (zikServers == null) {
            getServers();
        }
        stopMonitoringAllServerDeviceStreams();
        cancelMonitoringSetup = false;
        for (final ZIKServer zikServer : zikServers) {
            for (ZIKDevice liteZikDevice : zikServer.getDevices()) {
                final ZIKDevice zikDevice = liteZikDevice.fetchSync();
                for (final ZIKStream stream : zikDevice.getAllStreams()) {
                    if (cancelMonitoringSetup) {
                        return;
                    }
                    if (stream.getTitle().equals("logs")) {
                        continue;
                    }
                    monitor(stream, zikDevice, zikServer, listener);
                }
            }
        }
    }

    private void monitor(final ZIKStream stream, final ZIKDevice zikDevice, final ZIKServer zikServer, final ZikStreamEntryListener listener) {
        Observable.create(new Observable.OnSubscribe<ZIKStreamEntry>() {
            @Override
            public void call(final Subscriber<? super ZIKStreamEntry> subscriber) {
                stream.setStreamListener(new ZIKStreamListener() {
                    @Override
                    public void onUpdate(Object object) {
                        ZIKStreamEntry streamEntry = (ZIKStreamEntry) object;
                        subscriber.onNext(streamEntry);
                    }

                    @Override
                    public void onError(ZIKException exception, Response response) {
                        subscriber.onError(exception);
                    }

                    @Override
                    public void onClose() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onOpen() {
                        // not used
                    }

                    @Override
                    public void onPong() {
                        // not used
                    }
                });
                stream.resume();
                serverDevicesStreams.add(stream);
            }
        })
            .throttleLast(333, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(new Action1<ZIKStreamEntry>() {
                @Override
                public void call(ZIKStreamEntry zikStreamEntry) {
                    listener.updateFor(zikServer, zikDevice, zikStreamEntry);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Log.e(throwable, "Error whilst monitoring.");
                }
            });
    }

    public void stopMonitoringAllServerDeviceStreams() {
        cancelMonitoringSetup = true;
        for (ZIKStream stream : serverDevicesStreams) {
            stream.close();
        }
        serverDevicesStreams.clear();
    }

    private void monitorNonLogStreams(ZIKDevice device, ZikStreamEntryListener listener, List<ZIKStream> streamsCache) {
        List<ZIKStream> allStreams = device.getAllStreams();
        for (ZIKStream stream : allStreams) {
            if (cancelMonitoringSetup) {
                return;
            }
            if (stream.getTitle().equals("logs")) {
                continue;
            }
            monitor(stream, listener, device);
            streamsCache.add(stream);
        }
    }

    private void monitor(ZIKStream stream, final ZikStreamEntryListener listener, final ZIKDevice device) {
        ZIKDeviceId deviceId = device.getDeviceId();
        final ZIKServer server = getServerContaining(deviceId);
        stream.setStreamListener(new ZIKStreamListener() {
            @Override
            public void onOpen() {
                // not used
            }

            @Override
            public void onUpdate(Object object) {
                ZIKStreamEntry streamEntry = (ZIKStreamEntry) object;
                listener.updateFor(server, device, streamEntry);
            }

            @Override
            public void onPong() {
                // not used
            }

            @Override
            public void onClose() {
                // not used
            }

            @Override
            public void onError(ZIKException exception, Response response) {
                Log.e(exception, "Error streaming " + response);
            }
        });
        stream.resume();
    }

    public void startMonitoringLogStreamFor(ZIKDeviceId deviceId, ZikLogStreamEntryListener listener) {
        ZIKDevice device = getLiteDevice(deviceId);
        monitorLogStreams(device, listener);
    }

    private void monitorLogStreams(ZIKDevice device, ZikLogStreamEntryListener listener) {
        logStream = device.stream("logs");
        monitorLog(logStream, listener, device);
    }

    private void monitorLog(ZIKStream stream, final ZikLogStreamEntryListener listener, final ZIKDevice device) {
        ZIKDeviceId deviceId = device.getDeviceId();
        final ZIKServer server = getServerContaining(deviceId);
        stream.setStreamListener(new ZIKStreamListener() {
            @Override
            public void onOpen() {
                // not used
            }

            @Override
            public void onUpdate(Object object) {
                ZIKLogStreamEntry streamEntry = (ZIKLogStreamEntry) object;
                listener.updateFor(server, device, streamEntry);
            }

            @Override
            public void onPong() {
                // not used
            }

            @Override
            public void onClose() {
                // not used
            }

            @Override
            public void onError(ZIKException exception, Response response) {
                Log.e(exception, "Error streaming " + response);
            }
        });
        stream.resume();
    }

    public void stopMonitoringLogStream() {
        if (logStream == null) {
            return;
        }
        logStream.close();
    }

    /**
     * For use when somethings FooBar'd and you want to tidy up
     */
    public void reset() {
        cancelMonitoringSetup = true;
        stopMonitoringDeviceStreams();
        stopMonitoringAllServerDeviceStreams();
        stopMonitoringLogStream();
        cancelMonitoringSetup = false;
        if (zikServers != null) {
            zikServers.clear();
        }
        if (zikDevices != null) {
            zikDevices.clear();
        }
    }

    public interface ZikStreamEntryListener {
        void updateFor(ZIKServer server, ZIKDevice device, ZIKStreamEntry entry);
    }

    public interface ZikLogStreamEntryListener {
        void updateFor(ZIKServer server, ZIKDevice device, ZIKLogStreamEntry entry);
    }
}
