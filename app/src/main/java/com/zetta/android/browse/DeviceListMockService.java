package com.zetta.android.browse;

import android.graphics.Color;
import android.net.Uri;

import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.ZettaDeviceId;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

class DeviceListMockService {

    public List<ListItem> getListItems(String url) {
        int defaultColor = Color.parseColor("#f2f2f2");
        int banglorForegroundColor = Color.parseColor("#0000ff");
        int newOrleansForegroundColor = Color.parseColor("#dd33ff");
        int detroitForegroundColor = Color.parseColor("#dd3322");
        int stageForegroundColor = Color.parseColor("#008822");

        return Arrays.asList(
            new ServerListItem(banglorForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor), "bangalor"),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Door", "closed",
                               Uri.parse("http://www.zettaapi.org/icons/door-closed.png"),
                               banglorForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Photocell", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/photocell-ready.png"),
                               banglorForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Security System", "disarmed",
                               Uri.parse("http://www.zettaapi.org/icons/security-disarmed.png"),
                               banglorForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Window", "closed",
                               Uri.parse("http://www.zettaapi.org/icons/window-closed.png"),
                               banglorForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new ServerListItem(newOrleansForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor), "neworleans"),
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
            new ServerListItem(detroitForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor), "detroit"),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Motion1", "no-motion",
                               Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Thermometer1", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Camera1", "ready",
                               Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Motion2", "no-motion",
                               Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Thermometer2", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem(new ZettaDeviceId(UUID.randomUUID()), "Camera2", "ready",
                               Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
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
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new ServerListItem(stageForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor), "stage"),
            new EmptyServerListItem("No devices online for this server", ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor))
        );
    }
}
