package com.zetta.android.device.events;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.settings.SdkProperties;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class EventsService {

    private final SdkProperties sdkProperties;
    private final EventsSdkService sdkService;
    private final EventsMockService mockService;

    EventsService(SdkProperties sdkProperties, EventsSdkService sdkService, EventsMockService mockService) {
        this.sdkProperties = sdkProperties;
        this.sdkService = sdkService;
        this.mockService = mockService;
    }

    public void startMonitoringLogs(ZettaDeviceId deviceId, final StreamListener listener) {
        getLogsObservable(deviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ListItem>() {
                @Override
                public void onCompleted() {
                    // never completes - hot observable
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(e, "Something went wrong monitoring device specific streams.");
                }

                @Override
                public void onNext(ListItem listItem) {
                    listener.onUpdated(listItem);
                }
            });
    }

    private Observable<ListItem> getLogsObservable(final ZettaDeviceId deviceId) {
        return Observable.create(new Observable.OnSubscribe<ListItem>() {
            @Override
            public void call(final Subscriber<? super ListItem> subscriber) {
                monitorLogUpdates(deviceId, new StreamListener() {
                    @Override
                    public void onUpdated(ListItem listItem) {
                        subscriber.onNext(listItem);
                    }
                });
                // never completes - hot observable
            }
        });
    }

    private void monitorLogUpdates(ZettaDeviceId deviceId, StreamListener listener) {
        if (sdkProperties.useMockResponses()) {
            mockService.startMonitorLogUpdates(listener);
        } else {
            sdkService.startMonitorLogUpdatesFor(deviceId, listener);
        }
    }

    public void stopMonitoringLogs() {
        if (sdkProperties.useMockResponses()) {
            mockService.stopMonitoringStreamedUpdates();
        } else {
            sdkService.stopMonitoringStreamedUpdates();
        }
    }

    interface StreamListener {

        void onUpdated(ListItem listItem);

    }
}
