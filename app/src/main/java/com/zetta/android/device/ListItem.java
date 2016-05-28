package com.zetta.android.device;

import android.content.res.ColorStateList;

import java.net.URL;

interface ListItem {

    int TYPE_HEADER = 0;
    int TYPE_ACTION = 1;
    int TYPE_STREAM = 2;
    int TYPE_PROPERTY = 3;
    int TYPE_EVENTS = 4;
    int TYPE_STATE = 5;

    int getType();

    class HeaderListItem implements ListItem {

        private final String title;

        public HeaderListItem(String title) {
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

    class ActionListItem implements ListItem {

        private final String label;
        private final String action;
        private final ColorStateList foregroundColorList;
        private final ColorStateList backgroundColorList;

        public ActionListItem(String label, String action,
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

    class StreamListItem implements ListItem {

        private final String stream;
        private final String value;

        public StreamListItem(String stream, String value) {
            this.stream = stream;
            this.value = value;
        }

        @Override
        public int getType() {
            return TYPE_STREAM;
        }

        public String getStream() {
            return stream;
        }

        public String getValue() {
            return value;
        }
    }

    class PropertyListItem implements ListItem {

        private final String property;
        private final String value;

        public PropertyListItem(String property, String value) {
            this.property = property;
            this.value = value;
        }

        @Override
        public int getType() {
            return TYPE_PROPERTY;
        }

        public String getProperty() {
            return property;
        }

        public String getValue() {
            return value;
        }
    }

    class EventsListItem implements ListItem {

        private final String description;

        public EventsListItem(String description) {
            this.description = description;
        }

        @Override
        public int getType() {
            return TYPE_EVENTS;
        }

        public String getDescription() {
            return description;
        }
    }

    class StateListItem implements ListItem {

        private final String state;
        private final URL stateImageUrl;
        private final int foregroundColor;

        public StateListItem(String state, URL stateImageUrl, int foregroundColor) {
            this.state = state;
            this.stateImageUrl = stateImageUrl;
            this.foregroundColor = foregroundColor;
        }

        @Override
        public int getType() {
            return TYPE_STATE;
        }

        public String getState() {
            return state;
        }

        public URL getStateImageUrl() {
            return stateImageUrl;
        }

        public int getStateColor() {
            return foregroundColor;
        }
    }
}
