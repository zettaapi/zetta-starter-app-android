package com.zetta.android.settings;

import android.support.annotation.NonNull;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.ZettaSdkApi;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class SettingsService {

    @NonNull private final SdkProperties sdkProperties;

    public SettingsService(@NonNull SdkProperties sdkProperties) {
        this.sdkProperties = sdkProperties;
    }

    public void setRoot(@NonNull String url) {
        if (sdkProperties.useMockResponses()) {
            Log.v("Url set to " + url + " in demo mode this has no effect.");
        } else {
            getSetRootObservable(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        // not used
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(e, "Something went wrong setting the root url.");
                    }

                    @Override
                    public void onNext(Void blackhole) {
                        // do nothing
                    }
                });
        }
    }

    @NonNull
    private Observable<Void> getSetRootObservable(@NonNull final String url) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                ZettaSdkApi.INSTANCE.registerRoot(url);
                subscriber.onCompleted();
            }
        });
    }
}
