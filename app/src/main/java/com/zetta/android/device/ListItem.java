package com.zetta.android.device;

interface ListItem {

    int TYPE_HEADER = 0;
    int TYPE_ACTION = 1;
    int TYPE_STREAM = 2;
    int TYPE_PROPERTY = 3;
    int TYPE_EVENTS = 4;

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

        public ActionListItem(String label, String action) {
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
}
