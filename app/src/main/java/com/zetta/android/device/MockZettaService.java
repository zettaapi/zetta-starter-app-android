package com.zetta.android.device;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class MockZettaService {

    /**
     * TODO note this structure is only the UI structure and it is not what I expect to be return from the 'zetta library'
     */
    public static void getDetails(final Callback callback) {
        final List<ListItem> items = new ArrayList<>();

        items.add(new ListItem.ActionListItem("color", "set-color"));
        items.add(new ListItem.ActionListItem("brightness", "set-brightness"));
        items.add(new ListItem.ActionListItem("blink", "set-blinker"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));

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
