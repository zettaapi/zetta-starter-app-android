package com.zetta.android.browse;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.zetta.android.ListItem;
import com.zetta.android.settings.ApiUrlFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class ZettaService {

    private final ApiUrlFetcher apiUrlFetcher;

    ZettaService(ApiUrlFetcher apiUrlFetcher) {
        this.apiUrlFetcher = apiUrlFetcher;
    }

    public boolean hasRootUrl() {
        return apiUrlFetcher.hasUrl();
    }

    public String getRootUrl() {
        return apiUrlFetcher.getUrl();
    }

    public void getDeviceList(final Callback callback) {

        new Thread(new Runnable() {  // TODO change threading model
            @Override
            public void run() {
                String url = getRootUrl();
                final List<ListItem> items = new ArrayList<>();
                if (apiUrlFetcher.useMockResponses()) {
                    SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
                    items.addAll(MockZettaService.getListItems(url));
                } else {
                    items.addAll(SdkZettaService.getListItems(url));
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.on(items);
                    }
                });
            }
        }).start();
    }

    interface Callback {
        void on(List<ListItem> listItems);
    }

}
