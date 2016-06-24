package com.zetta.android.browse;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ZettaStyle;
import com.zetta.android.browse.DeviceListService.StreamListener;
import com.zetta.android.device.actions.ActionMultipleInputListItem;
import com.zetta.android.device.actions.ActionSingleInputListItem;
import com.zetta.android.device.actions.ActionToggleListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

class DeviceListMockService {

    private static final ZettaDeviceId DEVICE_ID = new ZettaDeviceId(UUID.fromString("86fee1a0-2fd1-11e6-a818-0002a5d5c51b"));
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#f2f2f2");

    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private ToggleStreamGenerator streamGenerator;

    public List<ListItem> getListItems(String url) {
        int banglorForegroundColor = Color.parseColor("#0000ff");
        int newOrleansForegroundColor = Color.parseColor("#dd33ff");
        int detroitForegroundColor = Color.parseColor("#dd3322");
        int stageForegroundColor = Color.parseColor("#008822");

        return Arrays.asList(
            new ServerListItem(banglorForegroundColor, getBackground(DEFAULT_BACKGROUND_COLOR), "bangalor"),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Door", "closed",
                               new ZettaStyle(
                                   banglorForegroundColor,
                                   DEFAULT_BACKGROUND_COLOR,
                                   Uri.parse("http://www.zettaapi.org/icons/door-closed.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Photocell", "ready",
                               new ZettaStyle(
                                   banglorForegroundColor,
                                   DEFAULT_BACKGROUND_COLOR,
                                   Uri.parse("http://www.zettaapi.org/icons/photocell-ready.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Security System", "disarmed",
                               new ZettaStyle(
                                   banglorForegroundColor,
                                   DEFAULT_BACKGROUND_COLOR,
                                   Uri.parse("http://www.zettaapi.org/icons/security-disarmed.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(DEVICE_ID, "Window", "closed",
                               new ZettaStyle(
                                   banglorForegroundColor,
                                   DEFAULT_BACKGROUND_COLOR,
                                   Uri.parse("http://www.zettaapi.org/icons/window-closed.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new ServerListItem(newOrleansForegroundColor, getBackground(DEFAULT_BACKGROUND_COLOR), "neworleans"),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Motion", "no-motion",
                               new ZettaStyle(
                                   newOrleansForegroundColor,
                                   Color.parseColor("#aaaaff"),
                                   Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Thermometer", "ready",
                               new ZettaStyle(
                                   newOrleansForegroundColor,
                                   Color.parseColor("#aaaaff"),
                                   Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Camera", "ready",
                               new ZettaStyle(
                                   newOrleansForegroundColor,
                                   Color.parseColor("#aaaaff"),
                                   Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                   ZettaStyle.TintMode.ORIGINAL
                               )
            ),
            new ServerListItem(detroitForegroundColor, getBackground(DEFAULT_BACKGROUND_COLOR), "detroit"),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Motion1", "no-motion",
                               new ZettaStyle(
                                   detroitForegroundColor,
                                   DEFAULT_BACKGROUND_COLOR,
                                   Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Thermometer1", "ready",
                               new ZettaStyle(
                                   detroitForegroundColor,
                                   DEFAULT_BACKGROUND_COLOR,
                                   Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Camera1", "ready",
                               new ZettaStyle(
                                   detroitForegroundColor,
                                   DEFAULT_BACKGROUND_COLOR,
                                   Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Motion2", "no-motion",
                               new ZettaStyle(
                                   detroitForegroundColor,
                                   DEFAULT_BACKGROUND_COLOR,
                                   Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Thermometer2", "ready",
                               new ZettaStyle(
                                   detroitForegroundColor,
                                   DEFAULT_BACKGROUND_COLOR,
                                   Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Camera2", "ready",
                               new ZettaStyle(
                                   detroitForegroundColor,
                                   DEFAULT_BACKGROUND_COLOR,
                                   Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Motion3", "no-motion",
                               new ZettaStyle(
                                   detroitForegroundColor,
                                   Color.parseColor("#236e4e"),
                                   Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Thermometer3", "ready",
                               new ZettaStyle(
                                   detroitForegroundColor,
                                   Color.parseColor("#111111"),
                                   Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Camera3", "ready",
                               new ZettaStyle(
                                   detroitForegroundColor,
                                   DEFAULT_BACKGROUND_COLOR,
                                   Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                   ZettaStyle.TintMode.TINTED
                               )
            ),
            new ServerListItem(stageForegroundColor, getBackground(DEFAULT_BACKGROUND_COLOR), "stage"),
            new ListItem.EmptyListItem("No devices online for this server", getBackground(DEFAULT_BACKGROUND_COLOR))
        );
    }

    public void startMonitorStreamedUpdates(String url, StreamListener listener) {
        streamGenerator = new ToggleStreamGenerator(mainThreadHandler, listener);
        mainThreadHandler.postDelayed(streamGenerator, TimeUnit.SECONDS.toMillis(1));
    }

    public List<ListItem> getQuickActions(ZettaDeviceId deviceId) {
        List<ListItem> items = new ArrayList<>();
        items.add(new ListItem.HeaderListItem("Door"));
        int foregroundColor = Color.parseColor("#1111dd");
        int backgroundColor = Color.parseColor("#d9d9d9");
        ColorStateList actionInputColorList = ColorStateList.valueOf(foregroundColor);
        ColorStateList actionTextColorList = ColorStateList.valueOf(backgroundColor);
        items.add(new ActionToggleListItem(deviceId, "open", "open", actionInputColorList, actionTextColorList, getBackground(foregroundColor), getBackground(backgroundColor)));
        items.add(new ActionSingleInputListItem(
            deviceId,
            "image...",
            "update-state-image",
            new ZettaStyle(foregroundColor, backgroundColor, Uri.EMPTY, ZettaStyle.TintMode.ORIGINAL)
        ));
        items.add(new ActionMultipleInputListItem(
            deviceId, Arrays.asList("color", "intensity"),
            "update-led",
            new ZettaStyle(foregroundColor, backgroundColor, Uri.EMPTY, ZettaStyle.TintMode.ORIGINAL)
        ));
        return items;
    }

    private static Drawable getBackground(int color) {
        return ImageLoader.Drawables.getSelectableDrawableFor(color);
    }

    private static class ToggleStreamGenerator implements Runnable {

        private final Handler handler;
        private final StreamListener listener;

        private boolean toggle;

        private ToggleStreamGenerator(Handler handler, StreamListener listener) {
            this.handler = handler;
            this.listener = listener;
        }

        @Override
        public void run() {
            String state;
            Uri icon;
            if (toggle) {
                toggle = false;
                icon = Uri.parse("http://www.zettaapi.org/icons/window-closed.png");
                state = "closed";
            } else {
                toggle = true;
                icon = Uri.parse("http://www.zettaapi.org/icons/window-open.png");
                state = "open";
            }
            listener.onUpdated(
                new DeviceListItem(
                    DEVICE_ID,
                    "Window",
                    state,
                    new ZettaStyle(
                        Color.parseColor("#0000ff"),
                        DEFAULT_BACKGROUND_COLOR,
                        icon,
                        ZettaStyle.TintMode.TINTED
                    )
                ));
            handler.postDelayed(this, TimeUnit.SECONDS.toMillis(1));
        }
    }

    public void stopMonitoringStreamedUpdates() {
        mainThreadHandler.removeCallbacks(streamGenerator);
    }
}
