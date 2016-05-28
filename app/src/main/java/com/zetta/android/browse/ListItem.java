package com.zetta.android.browse;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;

import java.net.URL;

interface ListItem {

    int TYPE_SERVER = 0;
    int TYPE_DEVICE = 1;
    int TYPE_EMPTY_SERVER = 2;
    int TYPE_HEADER = 3;
    int TYPE_ACTION = 4;

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

    class EmptyServerListItem implements ListItem {

        private final String message;

        public EmptyServerListItem(String message) {
            this.message = message;
        }

        @Override
        public int getType() {
            return TYPE_EMPTY_SERVER;
        }

        public String getMessage() {
            return message;
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
        private final ColorStateList foregroundColorList;
        private final ColorStateList backgroundColorList;

        public QuickActionListItem(String label,
                                   String action,
                                   ColorStateList foregroundColorList, ColorStateList backgroundColorList) {
            this.label = label;
            this.action = action;
            this.foregroundColorList = foregroundColorList;
            this.backgroundColorList = backgroundColorList;
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

        public ColorStateList getActionColorList() {
            return foregroundColorList;
        }

        public ColorStateList getActionTextColorList() {
            return backgroundColorList;
        }
    }
}
