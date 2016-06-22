package com.zetta.android.device.events;

import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import java.util.Date;
import java.util.concurrent.TimeUnit;

class EventsMockService {

    private static final int FOREGROUND_COLOR = Color.parseColor("#1111dd");
    private static final int BACKGROUND_COLOR = Color.parseColor("#d9d9d9");

    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private ToggleStreamGenerator streamGenerator;

    public void startMonitorLogUpdates(EventsService.StreamListener listener) {
        streamGenerator = new ToggleStreamGenerator(mainThreadHandler, listener);
        mainThreadHandler.postDelayed(streamGenerator, TimeUnit.SECONDS.toMillis(1));
    }

    private static class ToggleStreamGenerator implements Runnable {

        private final Handler handler;
        private final EventsService.StreamListener listener;

        private boolean toggle;

        private ToggleStreamGenerator(Handler handler, EventsService.StreamListener listener) {
            this.handler = handler;
            this.listener = listener;
        }

        @Override
        public void run() {
            String transition;
            Uri icon;
            if (toggle) {
                toggle = false;
                transition = "closed";
            } else {
                toggle = true;
                transition = "open";
            }
            listener.onUpdated(
                new EventListItem(
                    transition,
                    new Date(System.currentTimeMillis()).toString(),
                    FOREGROUND_COLOR,
                    BACKGROUND_COLOR
                ));
            handler.postDelayed(this, TimeUnit.SECONDS.toMillis(1));
        }
    }

    public void stopMonitoringStreamedUpdates() {
        mainThreadHandler.removeCallbacks(streamGenerator);
    }

}
