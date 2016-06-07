package com.zetta.android.device;

import com.zetta.android.ListItem;

import java.util.Arrays;
import java.util.List;

class DeviceDetailsSdkService {

    public DeviceDetailsService.Device getDetails(String url) {

        return new DeviceDetailsService.Device() {
            @Override
            public String getName() {
                return "Fake Light";
            }

            @Override
            public String getSeverName() {
                return "liverpool";
            }

            @Override
            public List<ListItem> getListItems() {
                return Arrays.<ListItem>asList(new ListItem.HeaderListItem("Not implemented yet"));
            }
        };
    }

}
