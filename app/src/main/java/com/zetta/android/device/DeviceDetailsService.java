package com.zetta.android.device;

import android.os.SystemClock;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ListItem;
import com.zetta.android.settings.ApiUrlFetcher;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class DeviceDetailsService {

    private final ApiUrlFetcher apiUrlFetcher;

    DeviceDetailsService(ApiUrlFetcher apiUrlFetcher) {
        this.apiUrlFetcher = apiUrlFetcher;
    }

    public void getDetails(final Callback callback) {
        getDetailsObservable()
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

    private Observable<Device> getDetailsObservable() {
        return Observable.create(new Observable.OnSubscribe<Device>() {
            @Override
            public void call(Subscriber<? super Device> subscriber) {
                subscriber.onNext(getDetails());
                subscriber.onCompleted();
            }
        });
    }

    private Device getDetails() {
        if (apiUrlFetcher.useMockResponses()) {
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));
            return DeviceDetailsMockService.getDetails();
        } else {
            return DeviceDetailsSdkService.getDetails();
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
}
