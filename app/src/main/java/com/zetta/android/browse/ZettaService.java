package com.zetta.android.browse;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.zetta.android.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class ZettaService {

    private static final boolean USE_MOCK = false;

    public static void getListItems(final String url, final Callback callback) {

        new Thread(new Runnable() {  // TODO change threading model
            @Override
            public void run() {

                final List<ListItem> items = new ArrayList<>();
                if (USE_MOCK) {
                    SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
                    items.addAll(MockZettaService.getListItems(url));
                } else {
                    items.addAll(SdkZettaService.getListItems(url));
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.on(items);
                    }
                });
            }
        }).start();
    }

    interface Callback {
        void on(List<ListItem> listItems);
    }

}
