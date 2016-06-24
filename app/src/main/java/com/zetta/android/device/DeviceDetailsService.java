package com.zetta.android.device;

import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.text.Spannable;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.settings.SdkProperties;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.AsyncOnSubscribe;
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

    public void startMonitoringStreamedUpdatesFor(ZettaDeviceId deviceId, final StreamListener listener) {
        getStreamedUpdatesObservable(deviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ListItem>() {
                @Override
                public void onNext(ListItem listItem) {
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

    private Observable<ListItem> getStreamedUpdatesObservable(final ZettaDeviceId deviceId) {
        return Observable.create(new AsyncOnSubscribe<LatestStateListener, ListItem>() {
            @Override
            protected LatestStateListener generateState() {
                LatestStateListener latestStateListener = new LatestStateListener();
                monitorStreamedUpdates(deviceId, latestStateListener);
                return latestStateListener;
            }

            @Override
            protected LatestStateListener next(LatestStateListener state, long requested, Observer<Observable<? extends ListItem>> observer) {
                ListItem latest = state.getLatest();
                observer.onNext(Observable.just(latest));
                return state;
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
        Spannable getName();

        Spannable getSeverName();

        Drawable createBackground();

        List<ListItem> getListItems();

        @ColorInt
        int getTintColor();

        @ColorInt
        int getBackgroundColor();
    }

    interface StreamListener {

        void onUpdated(ListItem listItem);

    }

    private static class LatestStateListener implements StreamListener {

        private ListItem listItem;

        public ListItem getLatest() {
            return listItem;
        }

        @Override
        public void onUpdated(ListItem listItem) {
            this.listItem = listItem;
        }
    }
}
