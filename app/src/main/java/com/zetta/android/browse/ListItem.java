package com.zetta.android.browse;

import android.support.annotation.ColorInt;

import java.net.URL;

interface ListItem {

    int TYPE_SERVER = 0;
    int TYPE_DEVICE = 1;
    int TYPE_HEADER = 2;
    int TYPE_ACTION = 3;

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
        private final int stateImageColor;

        public DeviceListItem(String name, String state, URL stateImageUrl, int stateImageColor) {
            this.name = name;
            this.state = state;
            this.stateImageUrl = stateImageUrl;
            this.stateImageColor = stateImageColor;
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

        public int getStateImageColor() {
            return stateImageColor;
        }
    }

    class HeaderQuickActionsListItem implements ListItem {

        private final String title;

        public HeaderQuickActionsListItem(String title) {
            this.title = title;
        }

        @Override
        public int getType() {
            return TYPE_HEADER;
        }

        public String getTitle() {
            return title;
        }
    }

    class QuickActionListItem implements ListItem {

        private final String label;
        private final String action;

        public QuickActionListItem(String label, String action) {
            this.label = label;
            this.action = action;
        }

        @Override
        public int getType() {
            return TYPE_ACTION;
        }

        public String getLabel() {
            return label;
        }

        public String getAction() {
            return action;
        }
    }
}
