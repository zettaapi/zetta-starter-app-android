package com.zetta.android.browse;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
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

        @ColorInt
        private final int swatchColor;
        private final Drawable background;
        private final String name;

        public ServerListItem(@ColorInt int foregroundColor, Drawable background, String name) {
            this.swatchColor = foregroundColor;
            this.background = background;
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

        public Drawable getBackground() {
            return background;
        }
    }

    class DeviceListItem implements ListItem {

        private final String name;
        private final String state;
        private final URL stateImageUrl;
        @ColorInt
        private final int foregroundColor;
        private final Drawable background;

        public DeviceListItem(String name, String state, URL stateImageUrl, @ColorInt int foregroundColor, Drawable background) {
            this.name = name;
            this.state = state;
            this.stateImageUrl = stateImageUrl;
            this.foregroundColor = foregroundColor;
            this.background = background;
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

        @ColorInt
        public int getStateImageColor() {
            return foregroundColor;
        }

        public Drawable getBackground() {
            return background;
        }
    }

    class EmptyServerListItem implements ListItem {

        private final String message;
        private final Drawable background;

        public EmptyServerListItem(String message, Drawable background) {
            this.message = message;
            this.background = background;
        }

        @Override
        public int getType() {
            return TYPE_EMPTY_SERVER;
        }

        public String getMessage() {
            return message;
        }

        public Drawable getBackground() {
            return background;
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
