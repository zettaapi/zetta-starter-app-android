package com.zetta.android;

import android.support.annotation.NonNull;
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
import com.apigee.zettakit.interfaces.ZIKCallback;
import com.apigee.zettakit.interfaces.ZIKStreamListener;
import com.novoda.notils.exception.DeveloperError;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * This singleton is not _fully_ tested as thread safe
 */
public enum ZettaSdkApi {
    INSTANCE;

    @NonNull private final List<ZIKStream> serverDevicesStreams = Collections.synchronizedList(new ArrayList<ZIKStream>());

    @Nullable private String rootUrl;
    @Nullable private ZIKRoot zikRoot;
    @Nullable private List<ZIKServer> zikServers;
    @Nullable private List<ZIKDevice> zikDevices;
    @Nullable private ZIKStream logStream;

    public void registerRoot(@NonNull String url) {
        if (url.equals(this.rootUrl)) {
            // No need to re-register
            return;
        }
        this.rootUrl = url;
        getRoot();
    }

    @NonNull
    public ZIKRoot getRoot() {
        if (rootUrl == null) {
            throw new DeveloperError("Call registerRoot(url) before getRoot!");
        }
        final ZIKSession session = ZIKSession.getSharedSession();
        this.zikRoot = session.getRootSync(rootUrl);
        return zikRoot;
    }

    @NonNull
    public List<ZIKServer> getServers() {
        if (zikRoot == null || !zikRoot.getSelfLink().getHref().equals(rootUrl)) {
            getRoot();
        }
        ZIKSession session = ZIKSession.getSharedSession();
        List<ZIKServer> zikServers = session.getServersSync(zikRoot);
        filterHiddenDevices(zikServers);
        this.zikServers = zikServers;
        return this.zikServers;
    }

    private void filterHiddenDevices(List<ZIKServer> zikServers) {
        for (ZIKServer zikServer : zikServers) {
            filterHiddenDevices(zikServer, zikServer.getDevices());
        }
    }

    private void filterHiddenDevices(ZIKServer server, List<ZIKDevice> zikDevices) {
        if (server.getProperties().containsKey("style")) {
            Map entities = (Map) ((Map) server.getProperties().get("style")).get("entities");
            for (Object entityType : entities.keySet()) {
                Map deviceType = (Map) entities.get(entityType);
                if (deviceType.containsKey("display") && deviceType.get("display").equals("none")) {
                    for (Iterator<ZIKDevice> iterator = zikDevices.iterator(); iterator.hasNext(); ) {
                        ZIKDevice zikDevice = iterator.next();
                        if (zikDevice.getType().equals(entityType)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    @NonNull
    public List<ZIKDevice> getDevices(@NonNull ZIKServer zikServer) {
        if (zikServers == null) {
            throw new DeveloperError("Ensure getServers is called and completes successfully first!");
        }
        ZIKSession session = ZIKSession.getSharedSession();
        this.zikDevices = session.getDevicesSync(zikServer);
        return zikDevices;
    }

    @NonNull
    public ZIKDevice getLiteDevice(@NonNull ZIKDeviceId zikDeviceId) {
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

    @NonNull
    public ZIKDevice getFullDevice(@NonNull ZIKDeviceId zikDeviceId) {
        return getLiteDevice(zikDeviceId).fetchSync();
    }

    @NonNull
    public ZIKServer getServerContaining(@NonNull ZIKDeviceId zikDeviceId) {
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

    public void startMonitoringDevice(@NonNull ZIKDeviceId deviceId, ZikLogStreamEntryListener listener) {
        stopMonitoringLogStream();
        ZIKDevice device = getLiteDevice(deviceId);
        monitorLogStream(device, listener);
    }

    public void stopMonitoringDevice() {
        stopMonitoringLogStream();
    }

    public void startMonitoringAllServerDeviceStreams(@NonNull final ZikStreamEntryListener listener) {
        if (zikServers == null) {
            getServers();
        }
        stopMonitoringAllServerDeviceStreams();
        for (final ZIKServer zikServer : zikServers) {
            List<ZIKDevice> zikDevices = zikServer.getDevices();
            for (ZIKDevice liteZikDevice : zikDevices) {
                final ZIKDevice zikDevice = liteZikDevice.fetchSync();
                for (final ZIKStream stream : zikDevice.getAllStreams()) {
                    if (stream.getTitle().equals("logs")) {
                        continue;
                    }
                    monitor(stream, zikDevice, zikServer, listener);
                    serverDevicesStreams.add(stream);
                }
            }
        }
    }

    private void monitor(@NonNull final ZIKStream stream,
                         @NonNull final ZIKDevice zikDevice,
                         @NonNull final ZIKServer zikServer,
                         @NonNull final ZikStreamEntryListener listener) {
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
            }
        })
            .throttleLast(500, TimeUnit.MILLISECONDS)
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
        synchronized (serverDevicesStreams) {
            for (ZIKStream stream : serverDevicesStreams) {
                stream.close();
            }
            serverDevicesStreams.clear();
        }
    }

    public void startMonitoringLogStreamFor(@NonNull ZIKDeviceId deviceId, @NonNull ZikLogStreamEntryListener listener) {
        ZIKDevice device = getLiteDevice(deviceId);
        monitorLogStream(device, listener);
    }

    private void monitorLogStream(@NonNull ZIKDevice device, @NonNull ZikLogStreamEntryListener listener) {
        logStream = device.stream("logs");
        if (logStream == null) {
            Log.e("Stream not found, monitoring nothing.");
            return;
        }
        monitorLog(logStream, listener, device);
    }

    private void monitorLog(@NonNull ZIKStream stream,
                            @NonNull final ZikLogStreamEntryListener listener,
                            @NonNull final ZIKDevice device) {
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

    public void update(final ZIKDeviceId deviceId, final String action, final Map<String, Object> commands, final ZIKCallback<ZIKDevice> callback) {
        Observable.create(new Observable.OnSubscribe<ZIKDevice>() {
            @Override
            public void call(final Subscriber<? super ZIKDevice> subscriber) {
                getFullDevice(deviceId).transition(action, commands, new ZIKCallback<ZIKDevice>() {
                    @Override
                    public void onSuccess(@NonNull ZIKDevice result) {
                        subscriber.onNext(result);
                    }

                    @Override
                    public void onFailure(@NonNull ZIKException exception) {
                        subscriber.onError(exception);
                    }
                });
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<ZIKDevice>() {
                @Override
                public void call(ZIKDevice device) {
                    callback.onSuccess(device);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    callback.onFailure((ZIKException) throwable);
                }
            });
    }

    public interface ZikStreamEntryListener {
        void updateFor(@NonNull ZIKServer server, @NonNull ZIKDevice device, @NonNull ZIKStreamEntry entry);
    }

    public interface ZikLogStreamEntryListener {
        void updateFor(@NonNull ZIKServer server, @NonNull ZIKDevice device, @NonNull ZIKLogStreamEntry entry);
    }
}
