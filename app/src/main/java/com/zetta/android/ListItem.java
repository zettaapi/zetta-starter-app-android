package com.zetta.android;

public interface ListItem {

    int TYPE_SERVER = 0;
    int TYPE_EMPTY_SERVER = 2;
    int TYPE_DEVICE = 1;
    int TYPE_HEADER = 3;
    int TYPE_ACTION_ON_OFF = 4;
    int TYPE_ACTION_SINGLE_INPUT = 5;
    int TYPE_ACTION_MULTIPLE_INPUT = 6;
    int TYPE_STREAM = 7;
    int TYPE_PROPERTY = 8;
    int TYPE_EVENTS = 9;
    int TYPE_EVENT = 10;
    int TYPE_STATE = 11;

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

}
