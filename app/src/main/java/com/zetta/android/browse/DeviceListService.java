package com.zetta.android.browse;

import android.os.SystemClock;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ListItem;
import com.zetta.android.settings.SdkProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class DeviceListService {

    private final SdkProperties sdkProperties;

    DeviceListService(SdkProperties sdkProperties) {
        this.sdkProperties = sdkProperties;
    }

    public boolean hasRootUrl() {
        return sdkProperties.hasUrl();
    }

    public String getRootUrl() {
        return sdkProperties.getUrl();
    }

    public void getDeviceList(final Callback callback) {
        getDeviceListObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<List<ListItem>>() {
                @Override
                public void onCompleted() {
                    // not used
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(e, "Something went wrong retrieving list of devices.");
                }

                @Override
                public void onNext(List<ListItem> listItems) {
                    callback.on(listItems);
                }
            });
    }

    private Observable<List<ListItem>> getDeviceListObservable() {
        return Observable.create(new Observable.OnSubscribe<List<ListItem>>() {
            @Override
            public void call(Subscriber<? super List<ListItem>> subscriber) {
                subscriber.onNext(getDeviceListItems());
                subscriber.onCompleted();
            }
        });
    }

    private List<ListItem> getDeviceListItems() {
        String url = getRootUrl();
        final List<ListItem> items = new ArrayList<>();
        if (sdkProperties.useMockResponses()) {
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
            items.addAll(DeviceListMockService.getListItems(url));
        } else {
            items.addAll(DeviceListSdkService.getListItems(url));
        }
        return items;
    }

    interface Callback {
        void on(List<ListItem> listItems);
    }

}
