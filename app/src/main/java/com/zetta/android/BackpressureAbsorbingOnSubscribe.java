package com.zetta.android;

import android.support.annotation.NonNull;

import com.novoda.notils.logger.simple.Log;

import rx.Observer;
import rx.observables.SyncOnSubscribe;

public abstract class BackpressureAbsorbingOnSubscribe<T> extends SyncOnSubscribe<BackpressureAbsorbingOnSubscribe.LatestStateListener<T>, T> {
    @Override
    protected LatestStateListener<T> generateState() {
        final LatestStateListener<T> latestStateListener = new LatestStateListener<>();

        startAsync(new LatestStateListener<T>() {
            @Override
            public void onNext(@NonNull T listItem) {
                latestStateListener.onNext(listItem);
            }
        });

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
    protected LatestStateListener<T> next(LatestStateListener<T> state, Observer<? super T> observer) {
        T latest = state.getLatest();
        Log.d("Latest ");
        if (latest == null) {
            Log.d("complete");
            observer.onCompleted();
            return state;
        } else {
            Log.d("next");
            observer.onNext(latest);
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
