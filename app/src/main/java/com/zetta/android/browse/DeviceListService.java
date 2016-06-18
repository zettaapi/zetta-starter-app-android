package com.zetta.android.browse;

import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.settings.SdkProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.AsyncOnSubscribe;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

class DeviceListService {

    private final CompositeSubscription subscriptions = new CompositeSubscription();

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
        Subscription subscription = getDeviceListObservable()
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
        subscriptions.add(subscription);
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
                sdkService.reset();
                items.add(new ListItem.EmptyListItem(
                    "Something went wrong with the SDK.\nDeveloper needs to fix.\nGo into Settings and try 'demo mode' to see something working.\n" + e,
                    ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#ffffff"))
                ));
            }
        }
        return items;
    }

    public void getQuickActions(ZettaDeviceId deviceId, Callback callback) {
        List<ListItem> items = getQuickActions(deviceId);

        callback.on(items);
    }

    @NonNull
    private List<ListItem> getQuickActions(ZettaDeviceId deviceId) {
        if (sdkProperties.useMockResponses()) {
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));
            return mockService.getQuickActions(deviceId);
        } else {
            return sdkService.getQuickActions();
        }
    }

    interface Callback {
        void on(List<ListItem> listItems);
    }

    public void startMonitoringStreamedUpdates(final StreamListener listener) {
        Subscription subscription = getStreamedUpdatesObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ListItem>() {
                @Override
                public void onNext(ListItem listItem) {
                    listener.onUpdated(listItem);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(e, "Something went wrong monitoring device list streams.");
                }

                @Override
                public void onCompleted() {
                    // not used
                }
            });
        subscriptions.add(subscription);
    }

    private Observable<ListItem> getStreamedUpdatesObservable() {
        return Observable.create(new AsyncOnSubscribe<LatestStateListener, ListItem>() {
            @Override
            protected LatestStateListener generateState() {
                LatestStateListener latestStateListener = new LatestStateListener();
                monitorStreamedUpdates(latestStateListener);
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

    private void monitorStreamedUpdates(StreamListener listener) {
        String url = sdkProperties.getUrl();
        if (sdkProperties.useMockResponses()) {
            mockService.startMonitorStreamedUpdates(url, listener);
        } else {
            sdkService.startMonitorStreamedUpdates(url, listener);
        }
    }

    public void stopMonitoringStreamedUpdates() {
        subscriptions.clear();
        if (sdkProperties.useMockResponses()) {
            mockService.stopMonitoringStreamedUpdates();
        } else {
            sdkService.stopMonitoringStreamedUpdates();
        }
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
