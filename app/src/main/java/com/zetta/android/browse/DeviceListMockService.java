package com.zetta.android.browse;

import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.browse.DeviceListService.StreamListener;

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
                new ServerListItem(banglorForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR), "bangalor"),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Door", "closed",
                                   Uri.parse("http://www.zettaapi.org/icons/door-closed.png"),
                                   banglorForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Photocell", "ready",
                                   Uri.parse("http://www.zettaapi.org/icons/photocell-ready.png"),
                                   banglorForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Security System", "disarmed",
                                   Uri.parse("http://www.zettaapi.org/icons/security-disarmed.png"),
                                   banglorForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                ),
                new DeviceListItem(DEVICE_ID, "Window", "closed",
                                   Uri.parse("http://www.zettaapi.org/icons/window-closed.png"),
                                   banglorForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                ),
                new ServerListItem(newOrleansForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR), "neworleans"),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Motion", "no-motion",
                                   Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                   newOrleansForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Thermometer", "ready",
                                   Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                   newOrleansForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Camera", "ready",
                                   Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                   newOrleansForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
                ),
                new ServerListItem(detroitForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR), "detroit"),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Motion1", "no-motion",
                                   Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                   detroitForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Thermometer1", "ready",
                                   Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                   detroitForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Camera1", "ready",
                                   Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                   detroitForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Motion2", "no-motion",
                                   Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                   detroitForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Thermometer2", "ready",
                                   Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                   detroitForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Camera2", "ready",
                                   Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                   detroitForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Motion3", "no-motion",
                                   Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                   detroitForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#236e4e"))
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Thermometer3", "ready",
                                   Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                   detroitForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#111111"))
                ),
                new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Camera3", "ready",
                                   Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                   detroitForegroundColor,
                                   ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                ),
                new ServerListItem(stageForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR), "stage"),
                new ListItem.EmptyListItem("No devices online for this server", ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR))
        );
    }

    public void startMonitorStreamedUpdates(StreamListener listener) {
        streamGenerator = new ToggleStreamGenerator(mainThreadHandler, listener);
        mainThreadHandler.postDelayed(streamGenerator, TimeUnit.SECONDS.toMillis(1));
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
                            icon,
                            Color.parseColor("#0000ff"),
                            ImageLoader.Drawables.getBackgroundDrawableFor(DEFAULT_BACKGROUND_COLOR)
                    ));
            handler.postDelayed(this, TimeUnit.SECONDS.toMillis(1));
        }
    }

    public void stopMonitoringStreamedUpdates() {
        mainThreadHandler.removeCallbacks(streamGenerator);
    }
}
