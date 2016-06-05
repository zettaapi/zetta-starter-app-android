package com.zetta.android.device;

import android.os.SystemClock;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ListItem;
import com.zetta.android.settings.ApiUrlFetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class ZettaService {

    private final ApiUrlFetcher apiUrlFetcher;

    ZettaService(ApiUrlFetcher apiUrlFetcher) {
        this.apiUrlFetcher = apiUrlFetcher;
    }

    public boolean hasRootUrl() {
        return apiUrlFetcher.hasUrl();
    }

    public String getRootUrl() {
        return apiUrlFetcher.getUrl();
    }

    public void getDetails(final Callback callback) {
        getDeviceListObservable()
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

    private Observable<Device> getDeviceListObservable() {
        return Observable.create(new Observable.OnSubscribe<Device>() {
            @Override
            public void call(Subscriber<? super Device> subscriber) {
                subscriber.onNext(getDeviceListItems());
                subscriber.onCompleted();
            }
        });
    }

    private Device getDeviceListItems() {
        String url = getRootUrl();
        final List<ListItem> items = new ArrayList<>();
        if (apiUrlFetcher.useMockResponses()) {
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
            return MockZettaService.getDetails();
        } else {
            // TODO items.addAll(SdkZettaService.getListItems(url));
            return new Device() {
                @Override
                public String getName() {
                    return "Fake Light";
                }

                @Override
                public String getSeverName() {
                    return "my roof";
                }

                @Override
                public List<ListItem> getListItems() {
                    return Arrays.<ListItem>asList(new ListItem.HeaderListItem("Not implemented yet"));
                }
            };
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
