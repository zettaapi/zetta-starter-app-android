package com.zetta.android.device;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class MockZettaService {

    /**
     * TODO note this structure is only the UI structure and it is not what I expect to be return from the 'zetta library'
     */
    public static void getDetails(final Callback callback) {
        final List<ListItem> items = new ArrayList<>();

        try {
            int foregroundColor = Color.parseColor("#0000ff");
            int backgroundColor = Color.parseColor("#ffffff");
            ColorStateList foregroundColorList = ColorStateList.valueOf(foregroundColor);
            ColorStateList backgroundColorList = ColorStateList.valueOf(backgroundColor);
            items.add(new ListItem.StateListItem(
                "on",
                new URL("http://www.zettaapi.org/icons/light-on.png"),
                foregroundColor
            ));
            items.add(new ListItem.HeaderListItem("Actions"));
            items.add(new ListItem.ActionListItem("color", "set-color", foregroundColorList, backgroundColorList));
            items.add(new ListItem.ActionListItem("brightness", "set-brightness", foregroundColorList, backgroundColorList));
            items.add(new ListItem.ActionListItem("blink", "set-blinker", foregroundColorList, backgroundColorList));
            items.add(new ListItem.ActionListItem("turn-off", "turn-off", foregroundColorList, backgroundColorList));
            items.add(new ListItem.HeaderListItem("Streams"));
            items.add(new ListItem.StreamListItem("state", "on"));
            items.add(new ListItem.HeaderListItem("Properties"));
            items.add(new ListItem.PropertyListItem("type", "light"));
            items.add(new ListItem.PropertyListItem("style", ""));
            items.add(new ListItem.PropertyListItem("brightness", ""));
            items.add(new ListItem.PropertyListItem("name", "Porch Light"));
            items.add(new ListItem.PropertyListItem("id", "5113a9d2-0dfa-4061-8034-8cde5bbb41b2"));
            items.add(new ListItem.PropertyListItem("state", "on"));
            items.add(new ListItem.PropertyListItem("color", ""));
            items.add(new ListItem.PropertyListItem("blink", ""));
            items.add(new ListItem.HeaderListItem("Events"));
            items.add(new ListItem.EventsListItem("View Events (42)"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.on("Porch Light", "neworleans", items);
                    }
                });
            }
        }).start();
    }

    interface Callback {
        void on(String deviceName, String serverName, List<ListItem> listItems);
    }
}
