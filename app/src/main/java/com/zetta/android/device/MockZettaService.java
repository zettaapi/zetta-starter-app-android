package com.zetta.android.device;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.zetta.android.ListItem;
import com.zetta.android.device.actions.ActionMultipleInputListItem;
import com.zetta.android.device.actions.ActionSingleInputListItem;
import com.zetta.android.device.actions.ActionToggleListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

class MockZettaService {

    /**
     * TODO note this structure is only the UI structure and it is not what I expect to be return from the 'zetta library'
     */
    public static void getDetails(final Callback callback) {
        final List<ListItem> items = new ArrayList<>();

        int foregroundColor = Color.parseColor("#0000ff");
        int backgroundColor = Color.parseColor("#ffffff");
        ColorStateList foregroundColorList = ColorStateList.valueOf(foregroundColor);
        ColorStateList backgroundColorList = ColorStateList.valueOf(backgroundColor);
        items.add(new StateListItem(
            "on",
            Uri.parse("http://www.zettaapi.org/icons/light-on.png"),
            foregroundColor
        ));
        items.add(new ListItem.HeaderListItem("Actions"));
        items.add(new ActionToggleListItem("open", "open", foregroundColorList, backgroundColorList));
        items.add(new ActionSingleInputListItem("brightness", "set-brightness", foregroundColorList, backgroundColorList));
        items.add(new ActionToggleListItem("blink", "set-blinker", foregroundColorList, backgroundColorList));
        items.add(new ActionToggleListItem("turn-off", "turn-off", foregroundColorList, backgroundColorList));
        items.add(new ActionMultipleInputListItem(
            Arrays.asList("direction", "speed", "duration", "walking style", "warning message"),
            "walk",
            foregroundColorList, backgroundColorList
        ));
        items.add(new ListItem.HeaderListItem("Streams"));
        items.add(new StreamListItem("state", "on"));
        items.add(new ListItem.HeaderListItem("Properties"));
        items.add(new PropertyListItem("type", "light"));
        items.add(new PropertyListItem("style", ""));
        items.add(new PropertyListItem("brightness", ""));
        items.add(new PropertyListItem("name", "Porch Light"));
        items.add(new PropertyListItem("id", "5113a9d2-0dfa-4061-8034-8cde5bbb41b2"));
        items.add(new PropertyListItem("state", "on"));
        items.add(new PropertyListItem("color", ""));
        items.add(new PropertyListItem("blink", ""));
        items.add(new ListItem.HeaderListItem("Events"));
        items.add(new EventsListItem("View Events (42)"));
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
