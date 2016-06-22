package com.zetta.android.device.events;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.zetta.android.BuildConfig;
import com.zetta.android.ListItem;
import com.zetta.android.R;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.settings.SdkProperties;

public class EventsActivity extends AppCompatActivity {

    public static final String KEY_DEVICE_ID = BuildConfig.APPLICATION_ID + "/DEVICE_ID";

    private EventsListAdapter adapter;
    private EventsService eventsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Events);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        adapter = new EventsListAdapter();
        RecyclerView deviceListWidget = (RecyclerView) findViewById(R.id.events_list);
        deviceListWidget.setAdapter(adapter);
        deviceListWidget.setHasFixedSize(true);
        deviceListWidget.setLayoutManager(new LinearLayoutManager(this));

        SdkProperties sdkProperties = SdkProperties.newInstance(this);
        EventsSdkService sdkService = new EventsSdkService();
        EventsMockService mockService = new EventsMockService();
        eventsService = new EventsService(sdkProperties, sdkService, mockService);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZettaDeviceId deviceId = (ZettaDeviceId) getIntent().getSerializableExtra(KEY_DEVICE_ID);
        eventsService.startMonitoringLogs(deviceId, onEventsLoaded);
    }

    private final EventsService.StreamListener onEventsLoaded = new EventsService.StreamListener() {
        @Override
        public void onUpdated(ListItem listItem) {
            adapter.update(listItem);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    protected void onPause() {
        eventsService.stopMonitoringLogs();
        super.onPause();
    }
}
