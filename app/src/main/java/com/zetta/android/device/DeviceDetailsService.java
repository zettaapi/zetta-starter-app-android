package com.zetta.android.device;

import android.os.SystemClock;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.settings.SdkProperties;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class DeviceDetailsService {

    private final SdkProperties sdkProperties;
    private final DeviceDetailsSdkService sdkService;
    private final DeviceDetailsMockService mockService;

    DeviceDetailsService(SdkProperties sdkProperties, DeviceDetailsSdkService sdkService, DeviceDetailsMockService mockService) {
        this.sdkProperties = sdkProperties;
        this.sdkService = sdkService;
        this.mockService = mockService;
    }

    public void getDetails(ZettaDeviceId deviceId, final Callback callback) {
        getDetailsObservable(deviceId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<Device>() {
                @Override
                public void onCompleted() {
                    // not used
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(e, "Something went wrong retrieving device specifics.");
                }

                @Override
                public void onNext(Device device) {
                    callback.on(device);
                }
            });
    }

    private Observable<Device> getDetailsObservable(final ZettaDeviceId deviceId) {
        return Observable.create(new Observable.OnSubscribe<Device>() {
            @Override
            public void call(Subscriber<? super Device> subscriber) {
                subscriber.onNext(getDetails(deviceId));
                subscriber.onCompleted();
            }
        });
    }

    private Device getDetails(ZettaDeviceId deviceId) {
        if (sdkProperties.useMockResponses()) {
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));
            return mockService.getDetails();
        } else {
            return sdkService.getDeviceDetails(deviceId);
        }
    }

    public void startMonitoringStreamedUpdatesFor(ZettaDeviceId deviceId, StreamListener listener) {
        getStreamedUpdatesObservable(deviceId, listener)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe();
    }

    private Observable<Void> getStreamedUpdatesObservable(final ZettaDeviceId deviceId, final StreamListener listener) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                monitorStreamedUpdates(deviceId, listener);
                // never completes - hot observable
            }
        });
    }

    private void monitorStreamedUpdates(ZettaDeviceId deviceId, StreamListener listener) {
        if (sdkProperties.useMockResponses()) {
            mockService.startMonitorStreamedUpdates(listener);
        } else {
            sdkService.startMonitorStreamedUpdatesFor(deviceId, listener);
        }
    }

    public void stopMonitoringStreamedUpdates() {
        if (sdkProperties.useMockResponses()) {
            mockService.stopMonitoringStreamedUpdates();
        } else {
            sdkService.stopMonitoringStreamedUpdates();
        }
    }

    interface Callback {
        void on(Device device);
    }

    interface Device {
        String getName();

        String getSeverName();

        List<ListItem> getListItems();
    }

    interface StreamListener {

        void onUpdated(ListItem listItem);

    }
}
