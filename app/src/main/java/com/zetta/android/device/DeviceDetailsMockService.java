package com.zetta.android.device;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaStyle;
import com.zetta.android.device.actions.ActionMultipleInputListItem;
import com.zetta.android.device.actions.ActionSingleInputListItem;
import com.zetta.android.device.actions.ActionToggleListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

class DeviceDetailsMockService {

    private static final ZettaDeviceId DEVICE_ID = new ZettaDeviceId(UUID.fromString("86fee1a0-2fd1-11e6-a818-0002a5d5c51b"));

    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private RandomStreamGenerator streamGenerator;

    public DeviceDetailsService.Device getDetails() {
        final List<ListItem> items = new ArrayList<>();

        final int foregroundColor = Color.parseColor("#1111dd");
        final int backgroundColor = Color.parseColor("#d9d9d9");
        final ZettaStyle style = new ZettaStyle(foregroundColor, backgroundColor, Uri.EMPTY, ZettaStyle.TintMode.ORIGINAL);
        items.add(new StateListItem(
            "on",
            Uri.parse("http://www.zettaapi.org/icons/light-on.png"),
            foregroundColor
        ));

        items.add(new ListItem.HeaderListItem("Actions"));
        items.add(new ActionToggleListItem(
            DEVICE_ID,
            "open", "open",
            style
        ));
        items.add(new ActionSingleInputListItem(
            DEVICE_ID,
            "brightness", "set-brightness",
            style
        ));
        items.add(new ActionToggleListItem(
            DEVICE_ID,
            "blink", "set-blinker",
            style
        ));
        items.add(new ActionToggleListItem(
            DEVICE_ID,
            "turn-off", "turn-off",
            style
        ));
        items.add(new ActionMultipleInputListItem(
            DEVICE_ID, Arrays.asList("direction", "speed", "duration", "walking style", "warning message"),
            "walk",
            style
        ));

        items.add(new ListItem.HeaderListItem("Streams"));
        items.add(new StreamListItem(DEVICE_ID, "temperature", "23.872342385", style));

        items.add(new ListItem.HeaderListItem("Properties"));
        items.add(new PropertyListItem("type", "light", style));
        items.add(new PropertyListItem("style", "", style));
        items.add(new PropertyListItem("brightness", "", style));
        items.add(new PropertyListItem("name", "Porch Light", style));
        items.add(new PropertyListItem("id", "5113a9d2-0dfa-4061-8034-8cde5bbb41b2", style));
        items.add(new PropertyListItem("state", "on", style));
        items.add(new PropertyListItem("color", "", style));
        items.add(new PropertyListItem("blink", "", style));

        items.add(new ListItem.HeaderListItem("Events"));
        items.add(new EventsListItem(DEVICE_ID, "View Events (42)", style));

        return new DeviceDetailsService.Device() {
            @Override
            public Spannable getName() {
                Spannable name = new SpannableString("Porch Light");
                name.setSpan(new BackgroundColorSpan(backgroundColor), 0, name.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(foregroundColor);
                name.setSpan(foregroundColorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                return name;
            }

            @Override
            public Spannable getSeverName() {
                Spannable name = new SpannableString("neworleans");
                name.setSpan(new BackgroundColorSpan(backgroundColor), 0, name.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(foregroundColor);
                name.setSpan(foregroundColorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                return name;
            }

            @Override
            public Drawable createBackground() {
                return style.createBackgroundDrawable();
            }

            @Override
            public List<ListItem> getListItems() {
                return items;
            }

            @Override
            public int getTintColor() {
                return style.getTintColor();
            }

            @Override
            public int getBackgroundColor() {
                return style.getBackgroundColor();
            }
        };
    }

    public void startMonitorStreamedUpdates(DeviceDetailsService.StreamListener listener) {
        streamGenerator = new RandomStreamGenerator(mainThreadHandler, listener);
        mainThreadHandler.postDelayed(streamGenerator, TimeUnit.SECONDS.toMillis(1));
    }

    private static class RandomStreamGenerator implements Runnable {

        private final Random random = new Random();

        private final Handler handler;
        private final DeviceDetailsService.StreamListener listener;

        private RandomStreamGenerator(Handler handler, DeviceDetailsService.StreamListener listener) {
            this.handler = handler;
            this.listener = listener;
        }

        @Override
        public void run() {
            String value = "23." + random.nextInt();
            int foregroundColor = Color.parseColor("#1111dd");
            int backgroundColor = Color.parseColor("#d9d9d9");
            Uri image = Uri.EMPTY;
            ZettaStyle.TintMode tintMode = ZettaStyle.TintMode.ORIGINAL;
            ZettaStyle style = new ZettaStyle(foregroundColor, backgroundColor, image, tintMode);
            listener.onUpdated(new StreamListItem(DEVICE_ID, "temperature", value, style));
            handler.postDelayed(this, TimeUnit.SECONDS.toMillis(1));
        }
    }

    public void stopMonitoringStreamedUpdates() {
        mainThreadHandler.removeCallbacks(streamGenerator);
    }
}
