package com.zetta.android.browse;

import android.graphics.Color;
import android.support.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MockZettaService {
    @NonNull
    public static List<ListItem> getListItems() {
        List<ListItem> items;
        try {
            items = Arrays.asList(
                new ListItem.ServerListItem(Color.parseColor("#0000ff"), "bangalor"),
                new ListItem.DeviceListItem("Door", "closed", new URL("http://www.zettaapi.org/icons/door-closed.png")),
                new ListItem.DeviceListItem("Photocell", "ready", new URL("http://www.zettaapi.org/icons/photocell-ready.png")),
                new ListItem.DeviceListItem("Security System", "disarmed", new URL("http://www.zettaapi.org/icons/security-disarmed.png")),
                new ListItem.DeviceListItem("Window", "closed", new URL("http://www.zettaapi.org/icons/window-closed.png")),
                new ListItem.ServerListItem(Color.parseColor("#dd33ff"), "neworleans"),
                new ListItem.DeviceListItem("Motion", "no-motion", new URL("http://www.zettaapi.org/icons/motion-no-motion.png")),
                new ListItem.DeviceListItem("Thermometer", "ready", new URL("http://www.zettaapi.org/icons/thermometer-ready.png")),
                new ListItem.DeviceListItem("Camera", "ready", new URL("http://www.zettaapi.org/public/demo/detroit.jpg"))
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }
}
