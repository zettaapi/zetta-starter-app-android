package com.zetta.android.device;

import android.content.res.ColorStateList;

import java.net.URL;
import java.util.List;

interface ListItem {

    int TYPE_HEADER = 0;
    int TYPE_ACTION_ON_OFF = 1;
    int TYPE_ACTION_SINGLE_INPUT = 2;
    int TYPE_ACTION_MULTIPLE_INPUT = 3;
    int TYPE_STREAM = 4;
    int TYPE_PROPERTY = 5;
    int TYPE_EVENTS = 6;
    int TYPE_STATE = 7;

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

    class ActionOnOffListItem implements ListItem {

        private final String label;
        private final String action;
        private final ColorStateList foregroundColorList;
        private final ColorStateList backgroundColorList;

        public ActionOnOffListItem(String label,
                                   String action,
                                   ColorStateList foregroundColorList, ColorStateList backgroundColorList) {
            this.label = label;
            this.action = action;
            this.foregroundColorList = foregroundColorList;
            this.backgroundColorList = backgroundColorList;
        }

        @Override
        public int getType() {
            return TYPE_ACTION_ON_OFF;
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

    class ActionSingleInputListItem implements ListItem {

        private final String label;
        private final String action;
        private final ColorStateList foregroundColorList;
        private final ColorStateList backgroundColorList;

        public ActionSingleInputListItem(String label,
                                         String action,
                                         ColorStateList foregroundColorList, ColorStateList backgroundColorList) {
            this.label = label;
            this.action = action;
            this.foregroundColorList = foregroundColorList;
            this.backgroundColorList = backgroundColorList;
        }

        @Override
        public int getType() {
            return TYPE_ACTION_SINGLE_INPUT;
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

    class ActionMultipleInputListItem implements ListItem {

        private final List<String> labels;
        private final String action;
        private final ColorStateList foregroundColorList;
        private final ColorStateList backgroundColorList;

        public ActionMultipleInputListItem(List<String> labels,
                                           String action,
                                           ColorStateList foregroundColorList, ColorStateList backgroundColorList) {
            this.labels = labels;
            this.action = action;
            this.foregroundColorList = foregroundColorList;
            this.backgroundColorList = backgroundColorList;
        }

        @Override
        public int getType() {
            return TYPE_ACTION_MULTIPLE_INPUT;
        }

        public List<String> getLabels() {
            return labels;
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
