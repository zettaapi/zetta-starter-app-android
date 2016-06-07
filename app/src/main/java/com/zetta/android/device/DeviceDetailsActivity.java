package com.zetta.android.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.zetta.android.BuildConfig;
import com.zetta.android.ImageLoader;
import com.zetta.android.R;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.device.actions.OnActionClickListener;
import com.zetta.android.device.events.EventsActivity;
import com.zetta.android.settings.SdkProperties;

import java.util.Map;

public class DeviceDetailsActivity extends AppCompatActivity {

    public static final String KEY_DEVICE_ID = BuildConfig.APPLICATION_ID + "/DEVICE_ID";

    private DeviceDetailsService deviceDetailsService;
    private EmptyLoadingView emptyLoadingWidget;
    private DetailsListAdapter adapter;
    private RecyclerView detailsListWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SdkProperties sdkProperties = SdkProperties.newInstance(this);
        DeviceDetailsSdkService sdkService = new DeviceDetailsSdkService();
        deviceDetailsService = new DeviceDetailsService(sdkProperties, sdkService);

        setContentView(R.layout.device_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        emptyLoadingWidget = (EmptyLoadingView) findViewById(R.id.device_details_empty_view);
        adapter = new DetailsListAdapter(new ImageLoader(), onActionClickListener, onEventsClickListener);
        detailsListWidget = (RecyclerView) findViewById(R.id.device_details_list);
        detailsListWidget.setAdapter(adapter);
        detailsListWidget.setHasFixedSize(true);
        detailsListWidget.setLayoutManager(new LinearLayoutManager(this));
    }

    private final OnActionClickListener onActionClickListener = new OnActionClickListener() {
        @Override
        public void onActionClick(String label, boolean on) {
            Toast.makeText(DeviceDetailsActivity.this, "TODO clicked " + label + " " + on, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onActionClick(String label, String input) {
            Toast.makeText(DeviceDetailsActivity.this, "TODO clicked " + label + " " + input, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onActionClick(String label, Map<String, String> inputs) {
            Toast.makeText(DeviceDetailsActivity.this, "TODO clicked " + label + " " + inputs, Toast.LENGTH_LONG).show();
        }
    };

    private final DetailsListAdapter.OnEventsClickListener onEventsClickListener = new DetailsListAdapter.OnEventsClickListener() {
        @Override
        public void onEventsClick() {
            Intent intent = new Intent(DeviceDetailsActivity.this, EventsActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_details, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateState();
        ZettaDeviceId deviceId = (ZettaDeviceId) getIntent().getSerializableExtra(KEY_DEVICE_ID);
        deviceDetailsService.getDetails(deviceId, onDeviceDetailsLoaded);
    }

    private final DeviceDetailsService.Callback onDeviceDetailsLoaded = new DeviceDetailsService.Callback() {
        @Override
        public void on(DeviceDetailsService.Device device) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(device.getName());
            actionBar.setSubtitle(device.getSeverName());
            adapter.updateAll(device.getListItems());
            updateState();
        }
    };

    private void updateState() {
        if (adapter.isEmpty()) {
            emptyLoadingWidget.setStateLoading();
            emptyLoadingWidget.setVisibility(View.VISIBLE);
            detailsListWidget.setVisibility(View.GONE);
        } else {
            emptyLoadingWidget.setVisibility(View.GONE);
            detailsListWidget.setVisibility(View.VISIBLE);
        }
    }
}
