package com.zetta.android.browse;

import android.graphics.Color;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaStyle;
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

    @NonNull private final CompositeSubscription subscriptions = new CompositeSubscription();

    @NonNull private final SdkProperties sdkProperties;
    @NonNull private final DeviceListSdkService sdkService;
    @NonNull private final DeviceListMockService mockService;

    DeviceListService(@NonNull SdkProperties sdkProperties,
                      @NonNull DeviceListSdkService sdkService,
                      @NonNull DeviceListMockService mockService) {
        this.sdkProperties = sdkProperties;
        this.sdkService = sdkService;
        this.mockService = mockService;
    }

    public boolean hasRootUrl() {
        return sdkProperties.hasUrl() || sdkProperties.useMockResponses();
    }

    @NonNull
    public String getRootUrl() {
        return sdkProperties.getUrl();
    }

    public void getDeviceList(@NonNull final Callback callback) {
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

    @NonNull
    private Observable<List<ListItem>> getDeviceListObservable() {
        return Observable.create(new Observable.OnSubscribe<List<ListItem>>() {
            @Override
            public void call(Subscriber<? super List<ListItem>> subscriber) {
                subscriber.onNext(getDeviceListItems());
                subscriber.onCompleted();
            }
        });
    }

    @NonNull
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
                    new ZettaStyle(Color.parseColor("#ffffff"), Color.parseColor("f2f2f2"), Uri.EMPTY, ZettaStyle.TintMode.ORIGINAL)
                ));
            }
        }
        return items;
    }

    public void getQuickActions(@NonNull final ZettaDeviceId deviceId, @NonNull final Callback callback) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<ListItem>>() {
            @Override
            public void call(Subscriber<? super List<ListItem>> subscriber) {
                List<ListItem> items = getQuickActions(deviceId);
                subscriber.onNext(items);
                subscriber.onCompleted();
            }
        })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<List<ListItem>>() {
                @Override
                public void onCompleted() {
                    // not used
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(e, "Something went wrong retrieving quick actions.");
                }

                @Override
                public void onNext(List<ListItem> listItems) {
                    callback.on(listItems);
                }
            });
        subscriptions.add(subscription);
    }

    @NonNull
    private List<ListItem> getQuickActions(@NonNull ZettaDeviceId deviceId) {
        if (sdkProperties.useMockResponses()) {
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));
            return mockService.getQuickActions(deviceId);
        } else {
            return sdkService.getQuickActions(deviceId);
        }
    }

    interface Callback {
        void on(@NonNull List<ListItem> listItems);
    }

    public void startMonitoringStreamedUpdates(@NonNull final StreamListener listener) {
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

    @NonNull
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

    private void monitorStreamedUpdates(@NonNull StreamListener listener) {
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

        void onUpdated(@NonNull ListItem listItem);

    }

    private static class LatestStateListener implements StreamListener {

        private ListItem listItem;

        public ListItem getLatest() {
            return listItem;
        }

        @Override
        public void onUpdated(@NonNull ListItem listItem) {
            this.listItem = listItem;
        }
    }

}
