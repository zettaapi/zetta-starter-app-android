package com.zetta.android.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.zetta.android.R;

public class ApiUrlFetcher {

    private final SharedPreferences sharedPreferences;
    private final String apiUrlKey;
    private final String mockResponsesKey;

    public static ApiUrlFetcher newInstance(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String apiUrlKey = context.getString(R.string.key_api_url_with_history);
        String mockResponsesKey = context.getString(R.string.key_mock_responses);
        return new ApiUrlFetcher(sharedPreferences, apiUrlKey, mockResponsesKey);
    }

    ApiUrlFetcher(SharedPreferences sharedPreferences, String apiUrlKey, String mockResponsesKey) {
        this.sharedPreferences = sharedPreferences;
        this.apiUrlKey = apiUrlKey;
        this.mockResponsesKey = mockResponsesKey;
    }

    public boolean hasUrl() {
        return !"".equals(getUrl().trim());
    }

    public String getUrl() {
        String collection = sharedPreferences.getString(apiUrlKey, "");
        HistoryCollection historyCollection = new HistoryCollection();
        historyCollection.appendTail(collection);
        return historyCollection.getHead();
    }

    public boolean useMockResponses() {
        return sharedPreferences.getBoolean(mockResponsesKey, false);
    }

}
