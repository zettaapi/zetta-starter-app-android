package com.zetta.android;

import android.support.annotation.NonNull;

import com.novoda.notils.logger.simple.Log;

import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.Observer;
import rx.observables.AsyncOnSubscribe;

public abstract class BackpressureAbsorbingOnSubscribe extends AsyncOnSubscribe<BackpressureAbsorbingOnSubscribe.LatestStateListener, ListItem> {
    @Override
    protected LatestStateListener generateState() {
        final LatestStateListener latestStateListener = new LatestStateListener();
        final CountDownLatch latch = new CountDownLatch(1);

        startAsync(new LatestStateListener() {
            @Override
            public void onNext(@NonNull ListItem listItem) {
                latestStateListener.onNext(listItem);
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Log.e(e);
        }
        return latestStateListener;
    }

    /**
     * Expects you to start an asynchronous task, then notify the listener at least once
     * Not calling the listener will leave the observable thread blocked
     *
     * @param listener
     */
    public abstract void startAsync(LatestStateListener listener);

    @Override
    protected LatestStateListener next(LatestStateListener state,
                                       long requested,
                                       Observer<Observable<? extends ListItem>> observer) {
        ListItem latest = state.getLatest();
        if (latest == null) {
            observer.onCompleted();
            return state;
        } else {
            observer.onNext(Observable.just(latest));
        }
        return state;
    }

    public static class LatestStateListener {

        private ListItem listItem;

        public ListItem getLatest() {
            return listItem;
        }

        public void onNext(@NonNull ListItem listItem) {
            this.listItem = listItem;
        }
    }
}
