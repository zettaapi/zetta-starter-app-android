package com.zetta.android.device.events;

interface ListItem {

    class EventListItem implements ListItem {

        private final String transition;
        private final String timestamp;

        public EventListItem(String transition, String timestamp) {
            this.transition = transition;
            this.timestamp = timestamp;
        }

        public String getTransition() {
            return transition;
        }

        public String getTimeStamp() {
            return timestamp;
        }

    }

}
