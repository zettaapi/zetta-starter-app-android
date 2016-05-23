package com.zetta.android.browse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zetta.android.ImageLoader;
import com.zetta.android.R;
import com.zetta.android.device.DeviceDetailsActivity;
import com.zetta.android.settings.ApiUrlFetcher;
import com.zetta.android.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class DeviceListActivity extends AppCompatActivity {

    private ApiUrlFetcher apiUrlFetcher;
    private DeviceListAdapter adapter;

    private RecyclerView deviceListWidget;
    private EmptyLoadingView emptyLoadingWidget;
    private BottomSheetBehavior<? extends View> bottomSheetBehavior;
    private RecyclerView deviceQuickActionsWidget;
    private QuickActionsAdapter quickActionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiUrlFetcher = ApiUrlFetcher.newInstance(this);

        setContentView(R.layout.device_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        emptyLoadingWidget = (EmptyLoadingView) findViewById(R.id.device_list_empty_view);
        adapter = new DeviceListAdapter(new ImageLoader(), onDeviceClickListener);
        deviceListWidget = (RecyclerView) findViewById(R.id.device_list);
        deviceListWidget.setHasFixedSize(true);
        deviceListWidget.setLayoutManager(new LinearLayoutManager(this));
        deviceListWidget.setAdapter(adapter);
        quickActionsAdapter = new QuickActionsAdapter(onActionClickListener);
        deviceQuickActionsWidget = (RecyclerView) findViewById(R.id.device_list_bottom_sheet_quick_actions);
        deviceQuickActionsWidget.setAdapter(quickActionsAdapter);
        deviceQuickActionsWidget.setHasFixedSize(true);
        deviceQuickActionsWidget.setLayoutManager(new LinearLayoutManager(this));
        bottomSheetBehavior = BottomSheetBehavior.from(deviceQuickActionsWidget);
    }

    private final DeviceListAdapter.OnDeviceClickListener onDeviceClickListener = new DeviceListAdapter.OnDeviceClickListener() {
        @Override
        public void onDeviceClick() {
            Intent intent = new Intent(DeviceListActivity.this, DeviceDetailsActivity.class);
            startActivity(intent);
        }

        @Override
        public void onDeviceLongClick() {
            List<ListItem> items = new ArrayList<>();
            items.add(new ListItem.HeaderQuickActionsListItem("Door"));
            items.add(new ListItem.QuickActionListItem("open", "open"));
            items.add(new ListItem.QuickActionListItem("image...", "update-state-image"));
            quickActionsAdapter.updateAll(items);

            // Hack that fixes an issue with bottom sheet not showing recycler view data when opened
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }, 1);
        }
    };

    private final QuickActionsAdapter.OnActionClickListener onActionClickListener = new QuickActionsAdapter.OnActionClickListener() {
        @Override
        public void onActionClick(String label) {
            Toast.makeText(DeviceListActivity.this, "TODO " + label, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = apiUrlFetcher.getUrl();
        Log.d("xxx", "got url " + url);

        List<ListItem> items = MockZettaService.getListItems();
        adapter.updateAll(items);

        if (!items.isEmpty()) {
            emptyLoadingWidget.setVisibility(View.GONE);
            deviceListWidget.setVisibility(View.VISIBLE);
        } else if (items.isEmpty() && apiUrlFetcher.hasUrl()) {
            emptyLoadingWidget.setStateLoading(apiUrlFetcher.getUrl());
            emptyLoadingWidget.setVisibility(View.VISIBLE);
            deviceListWidget.setVisibility(View.GONE);
        } else {
            emptyLoadingWidget.setStateEmpty();
            emptyLoadingWidget.setVisibility(View.VISIBLE);
            deviceListWidget.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
