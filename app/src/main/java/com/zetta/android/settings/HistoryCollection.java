package com.zetta.android.settings;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class HistoryCollection {

    static final String SEPARATOR = "[";
    private static final String ESC_SEPARATOR = "\\[";

    private String collection;

    public boolean hasHeadItem(String item) {
        return getHead().equals(item);
    }

    public String getHead() {
        if (collection == null) {
            return "";
        }
        if (collection.contains(SEPARATOR)) {
            return collection.substring(collection.lastIndexOf(SEPARATOR) + 1, collection.length());
        } else {
            return collection;
        }
    }

    public void appendTail(String tail) {
        if (this.collection == null) {
            this.collection = tail;
        } else {
            if (this.collection.split(ESC_SEPARATOR).length >= 5) {
                this.collection = this.collection.substring(this.collection.indexOf(SEPARATOR) + 1, this.collection.length());
            }

            this.collection = this.collection + SEPARATOR + tail;
        }
    }

    public List<String> getHistory() {
        if (collection == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(collection.split(ESC_SEPARATOR));
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(collection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoryCollection)) {
            return false;
        }

        HistoryCollection that = (HistoryCollection) o;

        return collection.equals(that.collection);

    }

    @Override
    public int hashCode() {
        return collection.hashCode();
    }

    @Override
    public String toString() {
        return collection;
    }
}
