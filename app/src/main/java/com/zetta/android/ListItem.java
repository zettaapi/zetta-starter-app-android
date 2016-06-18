package com.zetta.android;

import android.graphics.drawable.Drawable;

public interface ListItem {

    int TYPE_SERVER = 0;
    int TYPE_EMPTY = 2;
    int TYPE_DEVICE = 1;
    int TYPE_HEADER = 3;
    int TYPE_ACTION_TOGGLE = 4;
    int TYPE_ACTION_SINGLE_INPUT = 5;
    int TYPE_ACTION_MULTIPLE_INPUT = 6;
    int TYPE_STREAM = 7;
    int TYPE_PROPERTY = 8;
    int TYPE_EVENTS = 9;
    int TYPE_EVENT = 10;
    int TYPE_STATE = 11;
    int TYPE_LOADING = 12;

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

    class EmptyListItem implements ListItem {

        private final String message;
        private final Drawable background;

        public EmptyListItem(String message, Drawable background) {
            this.message = message;
            this.background = background;
        }

        @Override
        public int getType() {
            return ListItem.TYPE_EMPTY;
        }

        public String getMessage() {
            return message;
        }

        public Drawable getBackground() {
            return background;
        }
    }

    class LoadingListItem implements ListItem {

        @Override
        public int getType() {
            return ListItem.TYPE_LOADING;
        }
    }
}
