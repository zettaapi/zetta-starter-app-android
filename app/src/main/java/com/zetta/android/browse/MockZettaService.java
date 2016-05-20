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
                new ListItem.DeviceListItem("Door", "closed",
                                            new URL("http://www.zettaapi.org/icons/door-closed.png"),
                                            Color.parseColor("#bbbb33")),
                new ListItem.DeviceListItem("Photocell", "ready",
                                            new URL("http://www.zettaapi.org/icons/photocell-ready.png"),
                                            Color.parseColor("#bbbb33")),
                new ListItem.DeviceListItem("Security System", "disarmed",
                                            new URL("http://www.zettaapi.org/icons/security-disarmed.png"),
                                            Color.parseColor("#bbbb33")),
                new ListItem.DeviceListItem("Window", "closed",
                                            new URL("http://www.zettaapi.org/icons/window-closed.png"),
                                            Color.parseColor("#bbbb33")),
                new ListItem.ServerListItem(Color.parseColor("#dd33ff"), "neworleans"),
                new ListItem.DeviceListItem("Motion", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            Color.parseColor("#ffcc33")),
                new ListItem.DeviceListItem("Thermometer", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            Color.parseColor("#ffcc33")),
                new ListItem.DeviceListItem("Camera", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            Color.parseColor("#ffcc33")),
                new ListItem.ServerListItem(Color.parseColor("#dd3322"), "detroit"),
                new ListItem.DeviceListItem("Motion1", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            Color.parseColor("#44cc33")),
                new ListItem.DeviceListItem("Thermometer1", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            Color.parseColor("#44cc33")),
                new ListItem.DeviceListItem("Camera1", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            Color.parseColor("#44cc33")),
                new ListItem.DeviceListItem("Motion2", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            Color.parseColor("#44cc33")),
                new ListItem.DeviceListItem("Thermometer2", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            Color.parseColor("#44cc33")),
                new ListItem.DeviceListItem("Camera2", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            Color.parseColor("#44cc33")),
                new ListItem.DeviceListItem("Motion3", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            Color.parseColor("#44cc33")),
                new ListItem.DeviceListItem("Thermometer3", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            Color.parseColor("#44cc33")),
                new ListItem.DeviceListItem("Camera3", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            Color.parseColor("#44cc33"))
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }
}
