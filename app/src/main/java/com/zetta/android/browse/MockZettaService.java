package com.zetta.android.browse;

import android.graphics.Color;
import android.net.Uri;

import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;

import java.util.Arrays;
import java.util.List;

class MockZettaService {

    public static List<ListItem> getListItems(String url) {
        int defaultColor = Color.parseColor("#f2f2f2");
        int banglorForegroundColor = Color.parseColor("#0000ff");
        int newOrleansForegroundColor = Color.parseColor("#dd33ff");
        int detroitForegroundColor = Color.parseColor("#dd3322");
        int stageForegroundColor = Color.parseColor("#008822");

        return Arrays.asList(
            new ServerListItem(banglorForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor), "bangalor"),
            new DeviceListItem("Door", "closed",
                               Uri.parse("http://www.zettaapi.org/icons/door-closed.png"),
                               banglorForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Photocell", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/photocell-ready.png"),
                               banglorForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Security System", "disarmed",
                               Uri.parse("http://www.zettaapi.org/icons/security-disarmed.png"),
                               banglorForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Window", "closed",
                               Uri.parse("http://www.zettaapi.org/icons/window-closed.png"),
                               banglorForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new ServerListItem(newOrleansForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor), "neworleans"),
            new DeviceListItem("Motion", "no-motion",
                               Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                               newOrleansForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
            ),
            new DeviceListItem("Thermometer", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                               newOrleansForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
            ),
            new DeviceListItem("Camera", "ready",
                               Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                               newOrleansForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#aaaaff"))
            ),
            new ServerListItem(detroitForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor), "detroit"),
            new DeviceListItem("Motion1", "no-motion",
                               Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Thermometer1", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Camera1", "ready",
                               Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Motion2", "no-motion",
                               Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Thermometer2", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Camera2", "ready",
                               Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new DeviceListItem("Motion3", "no-motion",
                               Uri.parse("http://www.zettaapi.org/icons/motion-no-motion.png"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#236e4e"))
            ),
            new DeviceListItem("Thermometer3", "ready",
                               Uri.parse("http://www.zettaapi.org/icons/thermometer-ready.png"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(Color.parseColor("#111111"))
            ),
            new DeviceListItem("Camera3", "ready",
                               Uri.parse("http://www.zettaapi.org/public/demo/detroit.jpg"),
                               detroitForegroundColor,
                               ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor)
            ),
            new ServerListItem(stageForegroundColor, ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor), "stage"),
            new EmptyServerListItem("No devices online for this server", ImageLoader.Drawables.getBackgroundDrawableFor(defaultColor))
        );
    }
}
