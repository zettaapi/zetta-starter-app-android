package com.zetta.android;

import android.support.annotation.NonNull;

import com.novoda.notils.logger.simple.Log;

import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.Observer;
import rx.observables.AsyncOnSubscribe;

public abstract class BackpressureAbsorbingOnSubscribe<T> extends AsyncOnSubscribe<BackpressureAbsorbingOnSubscribe.LatestStateListener<T>, T> {
    @Override
    protected LatestStateListener<T> generateState() {
        final LatestStateListener<T> latestStateListener = new LatestStateListener<>();
        final CountDownLatch latch = new CountDownLatch(1);

        startAsync(new LatestStateListener<T>() {
            @Override
            public void onNext(@NonNull T listItem) {
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
    public abstract void startAsync(LatestStateListener<T> listener);

    @Override
    protected LatestStateListener<T> next(LatestStateListener<T> state, long requested, Observer<Observable<? extends T>> observer) {
        T latest = state.getLatest();
        if (latest == null) {
            observer.onCompleted();
            return state;
        } else {
            observer.onNext(Observable.just(latest));
        }
        return state;
    }

    public static class LatestStateListener<T> {

        private T item;

        public T getLatest() {
            return item;
        }

        public void onNext(@NonNull T item) {
            this.item = item;
        }
    }
}
