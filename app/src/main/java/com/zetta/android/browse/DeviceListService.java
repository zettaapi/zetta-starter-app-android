package com.zetta.android.browse;

import android.graphics.Color;
import android.os.SystemClock;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ImageLoader;
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
    private final DeviceListSdkService sdkService;
    private final DeviceListMockService mockService;

    DeviceListService(SdkProperties sdkProperties, DeviceListSdkService sdkService, DeviceListMockService mockService) {
        this.sdkProperties = sdkProperties;
        this.sdkService = sdkService;
        this.mockService = mockService;
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
            items.addAll(mockService.getListItems(url));
        } else {
            try {
                items.addAll(sdkService.getListItems(url));
            } catch (Exception e) { // TODO remove this, just a temp measure
                Log.e(e);
                items.add(new EmptyServerListItem(
                    "Something went wrong with the SDK.\nDeveloper needs to fix.\nGo into Settings and try 'demo mode' to see something working.\n" + e,
                    ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#ffffff"))
                ));
            }
        }
        return items;
    }

    interface Callback {
        void on(List<ListItem> listItems);
    }

}
