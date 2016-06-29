package com.zetta.android.device;

import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.BackpressureAbsorbingOnSubscribe;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.settings.SdkProperties;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class DeviceDetailsService {

    @NonNull private final SdkProperties sdkProperties;
    @NonNull private final DeviceDetailsSdkService sdkService;
    @NonNull private final DeviceDetailsMockService mockService;

    DeviceDetailsService(@NonNull SdkProperties sdkProperties,
                         @NonNull DeviceDetailsSdkService sdkService,
                         @NonNull DeviceDetailsMockService mockService) {
        this.sdkProperties = sdkProperties;
        this.sdkService = sdkService;
        this.mockService = mockService;
    }

    public void getDetails(@NonNull ZettaDeviceId deviceId, @NonNull final Callback callback) {
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
                    callback.onDeviceLoadError();
                }

                @Override
                public void onNext(Device device) {
                    callback.on(device);
                }
            });
    }

    @NonNull
    private Observable<Device> getDetailsObservable(@NonNull final ZettaDeviceId deviceId) {
        return Observable.create(new Observable.OnSubscribe<Device>() {
            @Override
            public void call(Subscriber<? super Device> subscriber) {
                subscriber.onNext(getDetails(deviceId));
                subscriber.onCompleted();
            }
        });
    }

    @NonNull
    private Device getDetails(@NonNull ZettaDeviceId deviceId) {
        if (sdkProperties.useMockResponses()) {
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));
            return mockService.getDetails();
        } else {
            return sdkService.getDeviceDetails(deviceId);
        }
    }

    public void startMonitoringStreamedUpdatesFor(@NonNull ZettaDeviceId deviceId, @NonNull final StreamListener listener) {
        getStreamedUpdatesObservable(deviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<ListItem>>() {
                @Override
                public void onNext(List<ListItem> listItem) {
                    listener.onUpdated(listItem);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(e, "Something went wrong monitoring device specific streams.");
                }

                @Override
                public void onCompleted() {
                    // never completes - hot observable
                }
            });
    }

    @NonNull
    private Observable<List<ListItem>> getStreamedUpdatesObservable(@NonNull final ZettaDeviceId deviceId) {
        return Observable.create(new BackpressureAbsorbingOnSubscribe<List<ListItem>>() {
            @Override
            public void startAsync(final LatestStateListener<List<ListItem>> listener) {
                monitorStreamedUpdates(deviceId, new StreamListener() {

                    @Override
                    public void onUpdated(@NonNull List<ListItem> listItems) {
                        listener.onNext(listItems);
                    }
                });
            }
        });
    }

    private void monitorStreamedUpdates(@NonNull ZettaDeviceId deviceId, @NonNull StreamListener listener) {
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
        void on(@NonNull Device device);

        void onDeviceLoadError();
    }

    interface StreamListener {

        void onUpdated(@NonNull List<ListItem> listItems);

    }

}
