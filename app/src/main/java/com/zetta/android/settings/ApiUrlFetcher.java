package com.zetta.android.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.zetta.android.R;

public class ApiUrlFetcher {

    private final SharedPreferences sharedPreferences;
    private final String key;

    public static ApiUrlFetcher newInstance(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.key_api_url_with_history);
        return new ApiUrlFetcher(sharedPreferences, key);
    }

    ApiUrlFetcher(SharedPreferences sharedPreferences, String key) {
        this.sharedPreferences = sharedPreferences;
        this.key = key;
    }

    public boolean hasUrl() {
        return !"".equals(getUrl().trim());
    }

    public String getUrl() {
        String collection = sharedPreferences.getString(key, "");
        HistoryCollection historyCollection = new HistoryCollection();
        historyCollection.appendTail(collection);
        return historyCollection.getHead();
    }

}
