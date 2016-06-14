package com.zetta.android.browse;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.BuildConfig;
import com.zetta.android.ZettaDeviceId;
import com.zetta.android.ImageLoader;
import com.zetta.android.ListItem;
import com.zetta.android.R;
import com.zetta.android.device.DeviceDetailsActivity;
import com.zetta.android.device.actions.ActionMultipleInputListItem;
import com.zetta.android.device.actions.ActionSingleInputListItem;
import com.zetta.android.device.actions.ActionToggleListItem;
import com.zetta.android.device.actions.OnActionClickListener;
import com.zetta.android.settings.SdkProperties;
import com.zetta.android.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DeviceListActivity extends AppCompatActivity {

    static {
        Log.setShowLogs(BuildConfig.DEBUG);
    }

    private DeviceListService deviceListService;
    private DeviceListAdapter adapter;
    private RecyclerView deviceListWidget;
    private EmptyLoadingView emptyLoadingWidget;
    private BottomSheetBehavior<? extends View> bottomSheetBehavior;
    private QuickActionsAdapter quickActionsAdapter;
    private SwipeRefreshLayout pullRefreshWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SdkProperties sdkProperties = SdkProperties.newInstance(this);
        DeviceListSdkService sdkService = new DeviceListSdkService();
        DeviceListMockService mockService = new DeviceListMockService();
        deviceListService = new DeviceListService(sdkProperties, sdkService, mockService);

        setContentView(R.layout.device_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        emptyLoadingWidget = (EmptyLoadingView) findViewById(R.id.device_list_empty_view);
        adapter = new DeviceListAdapter(new ImageLoader(), onDeviceClickListener);
        deviceListWidget = (RecyclerView) findViewById(R.id.device_list);
        deviceListWidget.setAdapter(adapter);
        deviceListWidget.setHasFixedSize(true);
        deviceListWidget.setLayoutManager(new LinearLayoutManager(this));
        quickActionsAdapter = new QuickActionsAdapter(onActionClickListener);
        RecyclerView deviceQuickActionsWidget = (RecyclerView) findViewById(R.id.device_list_bottom_sheet_quick_actions);
        deviceQuickActionsWidget.setAdapter(quickActionsAdapter);
        deviceQuickActionsWidget.setHasFixedSize(true);
        deviceQuickActionsWidget.setLayoutManager(new LinearLayoutManager(this));
        bottomSheetBehavior = BottomSheetBehavior.from(deviceQuickActionsWidget);
        pullRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.pull_refresh);
        pullRefreshWidget.setOnRefreshListener(onPullRefreshListener);
    }

    private final DeviceListAdapter.OnDeviceClickListener onDeviceClickListener = new DeviceListAdapter.OnDeviceClickListener() {
        @Override
        public void onDeviceClick(ZettaDeviceId deviceId) {
            Intent intent = new Intent(DeviceListActivity.this, DeviceDetailsActivity.class);
            intent.putExtra(DeviceDetailsActivity.KEY_DEVICE_ID, deviceId);
            startActivity(intent);
        }

        @Override
        public void onDeviceLongClick(ZettaDeviceId deviceId) {
            List<ListItem> items = new ArrayList<>();
            items.add(new ListItem.HeaderListItem("Door"));
            int foregroundColor = Color.parseColor("#1111dd");
            int backgroundColor = Color.parseColor("#d9d9d9");
            ColorStateList actionTextColorList = ColorStateList.valueOf(backgroundColor);
            items.add(new ActionToggleListItem("open", "open", actionTextColorList, getBackground(foregroundColor)));
            items.add(new ActionSingleInputListItem("image...", "update-state-image", actionTextColorList, getBackground(foregroundColor)));
            items.add(new ActionMultipleInputListItem(
                Arrays.asList("color", "intensity"),
                "update-led",
                actionTextColorList, getBackground(foregroundColor)
            ));
            quickActionsAdapter.updateAll(items);

            // This postDelayed is a hack that fixes an issue with bottom sheet not showing recycler view data when opened
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }, 1);
        }

        private Drawable getBackground(int foregroundColor) {
            return ImageLoader.Drawables.getBackgroundDrawableFor(foregroundColor);
        }
    };

    private final OnActionClickListener onActionClickListener = new OnActionClickListener() {
        @Override
        public void onActionClick(String label, boolean on) {
            Toast.makeText(DeviceListActivity.this, "TODO clicked " + label + " " + on, Toast.LENGTH_SHORT).show();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        @Override
        public void onActionClick(String label, String input) {
            Toast.makeText(DeviceListActivity.this, "TODO clicked " + label + " " + input, Toast.LENGTH_SHORT).show();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        @Override
        public void onActionClick(String label, Map<String, String> inputs) {
            Toast.makeText(DeviceListActivity.this, "TODO clicked " + label + " " + inputs, Toast.LENGTH_LONG).show();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    };

    private final SwipeRefreshLayout.OnRefreshListener onPullRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Toast.makeText(DeviceListActivity.this, "TODO Refresh list", Toast.LENGTH_SHORT).show();
            deviceListService.getDeviceList(onDeviceListLoaded);
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
        updateState();
        deviceListService.getDeviceList(onDeviceListLoaded);
        deviceListService.startMonitoringStreamedUpdates(onStreamedUpdate);
    }

    private final DeviceListService.StreamListener onStreamedUpdate = new DeviceListService.StreamListener() {
        @Override
        public void onUpdated(ListItem listItem) {
            if (deviceListWidget.isComputingLayout() || deviceListWidget.isAnimating()) {
                return;
            }
            adapter.update(listItem);
        }
    };

    private final DeviceListService.Callback onDeviceListLoaded = new DeviceListService.Callback() {
        @Override
        public void on(List<ListItem> listItems) {
            adapter.updateAll(listItems);
            pullRefreshWidget.setRefreshing(false);
            updateState();
        }
    };

    private void updateState() {
        if (!adapter.isEmpty()) {
            emptyLoadingWidget.setVisibility(View.GONE);
            deviceListWidget.setVisibility(View.VISIBLE);
        } else if (adapter.isEmpty() && deviceListService.hasRootUrl()) {
            emptyLoadingWidget.setStateLoading(deviceListService.getRootUrl());
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

    @Override
    protected void onPause() {
        deviceListService.stopMonitoringStreamedUpdates();
        super.onPause();
    }
}
