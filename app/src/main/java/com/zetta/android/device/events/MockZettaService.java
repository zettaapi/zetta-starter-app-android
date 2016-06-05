package com.zetta.android.device.events;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.zetta.android.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class MockZettaService {

    /**
     * TODO note this structure is only the UI structure and it is not what I expect to be return from the 'zetta library'
     */
    public static void getEvents(final Callback callback) {
        final List<ListItem> items = new ArrayList<>();

        items.add(new EventListItem("turn-off", "5/23/16, 6:33:23PM"));
        items.add(new EventListItem("turn-on", "5/23/16, 6:33:21PM"));
        items.add(new EventListItem("turn-off", "5/23/16, 6:33:19PM"));
        items.add(new EventListItem("turn-on", "5/23/16, 6:33:17PM"));
        items.add(new EventListItem("turn-off", "5/23/16, 6:33:15PM"));
        items.add(new EventListItem("turn-on", "5/23/16, 6:33:13PM"));
        items.add(new EventListItem("turn-off", "5/23/16, 6:33:11PM"));
        items.add(new EventListItem("turn-on", "5/23/16, 6:33:09PM"));
        items.add(new EventListItem("turn-off", "5/23/16, 6:33:07PM"));
        items.add(new EventListItem("turn-on", "5/23/16, 6:33:05PM"));
        items.add(new EventListItem("turn-off", "5/23/16, 6:33:03PM"));
        items.add(new EventListItem("turn-on", "5/23/16, 6:33:01PM"));
        items.add(new EventListItem("turn-off", "5/23/16, 6:32:59PM"));
        items.add(new EventListItem("turn-on", "5/23/16, 6:32:57PM"));
        items.add(new EventListItem("turn-off", "5/23/16, 6:32:55PM"));
        items.add(new EventListItem("turn-on", "5/23/16, 6:32:53PM"));
        items.add(new EventListItem("turn-off", "5/23/16, 6:32:51PM"));
        items.add(new EventListItem("turn-on", "5/23/16, 6:32:49PM"));
        items.add(new EventListItem("turn-off", "5/23/16, 6:32:47PM"));
        items.add(new EventListItem("turn-on", "5/23/16, 6:32:45PM"));
        items.add(new EventListItem("turn-off", "5/23/16, 6:32:43PM"));
        items.add(new EventListItem("turn-on", "5/23/16, 6:32:41PM"));

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
