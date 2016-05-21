package com.zetta.android.browse;

import android.graphics.Color;
import android.support.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MockZettaService {

    /**
     * TODO note this structure is only the UI structure and it is not what I expect to be return from the 'zetta library'
     *
     * @return
     */
    @NonNull
    public static List<ListItem> getListItems() {
        int banglorColor = Color.parseColor("#0000ff");
        int newOrleansColor = Color.parseColor("#dd33ff");
        int detroitColor = Color.parseColor("#dd3322");

        List<ListItem> items;
        try {
            items = Arrays.asList(
                new ListItem.ServerListItem(banglorColor, "bangalor"),
                new ListItem.DeviceListItem("Door", "closed",
                                            new URL("http://www.zettaapi.org/icons/door-closed.png"),
                                            banglorColor
                ),
                new ListItem.DeviceListItem("Photocell", "ready",
                                            new URL("http://www.zettaapi.org/icons/photocell-ready.png"),
                                            banglorColor
                ),
                new ListItem.DeviceListItem("Security System", "disarmed",
                                            new URL("http://www.zettaapi.org/icons/security-disarmed.png"),
                                            banglorColor
                ),
                new ListItem.DeviceListItem("Window", "closed",
                                            new URL("http://www.zettaapi.org/icons/window-closed.png"),
                                            banglorColor
                ),
                new ListItem.ServerListItem(newOrleansColor, "neworleans"),
                new ListItem.DeviceListItem("Motion", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            newOrleansColor
                ),
                new ListItem.DeviceListItem("Thermometer", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            newOrleansColor
                ),
                new ListItem.DeviceListItem("Camera", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            newOrleansColor
                ),
                new ListItem.ServerListItem(detroitColor, "detroit"),
                new ListItem.DeviceListItem("Motion1", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            detroitColor
                ),
                new ListItem.DeviceListItem("Thermometer1", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            detroitColor
                ),
                new ListItem.DeviceListItem("Camera1", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            detroitColor
                ),
                new ListItem.DeviceListItem("Motion2", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            detroitColor
                ),
                new ListItem.DeviceListItem("Thermometer2", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            detroitColor
                ),
                new ListItem.DeviceListItem("Camera2", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            detroitColor
                ),
                new ListItem.DeviceListItem("Motion3", "no-motion",
                                            new URL("http://www.zettaapi.org/icons/motion-no-motion.png"),
                                            detroitColor
                ),
                new ListItem.DeviceListItem("Thermometer3", "ready",
                                            new URL("http://www.zettaapi.org/icons/thermometer-ready.png"),
                                            detroitColor
                ),
                new ListItem.DeviceListItem("Camera3", "ready",
                                            new URL("http://www.zettaapi.org/public/demo/detroit.jpg"),
                                            detroitColor
                )
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }
}
