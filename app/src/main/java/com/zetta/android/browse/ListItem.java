package com.zetta.android.browse;

import android.support.annotation.ColorInt;

import java.net.URL;

interface ListItem {

    int TYPE_SERVER = 0;
    int TYPE_DEVICE = 1;

    int getType();

    class ServerListItem implements ListItem {

        private final int swatchColor;
        private final String name;

        public ServerListItem(@ColorInt int swatchColor, String name) {
            this.swatchColor = swatchColor;
            this.name = name;
        }

        @Override
        public int getType() {
            return TYPE_SERVER;
        }

        @ColorInt
        public int getSwatchColor() {
            return swatchColor;
        }

        public String getName() {
            return name;
        }
    }

    class DeviceListItem implements ListItem {

        private final String name;
        private final String state;
        private final URL stateImageUrl;

        public DeviceListItem(String name, String state, URL stateImageUrl) {
            this.name = name;
            this.state = state;
            this.stateImageUrl = stateImageUrl;
        }

        @Override
        public int getType() {
            return TYPE_DEVICE;
        }

        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }

        public URL getStateImageUrl() {
            return stateImageUrl;
        }
    }
}
