package com.zetta.android.device;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.zetta.android.ImageLoader;
import com.zetta.android.R;

import java.util.List;

public class DeviceDetailsActivity extends AppCompatActivity {

    private DetailsListAdapter adapter;
    private RecyclerView detailsListWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = new DetailsListAdapter(new ImageLoader(), onActionClickListener);
        detailsListWidget = (RecyclerView) findViewById(R.id.device_details_list);
        detailsListWidget.setAdapter(adapter);
        detailsListWidget.setHasFixedSize(true);
        detailsListWidget.setLayoutManager(new LinearLayoutManager(this));
    }

    private final DetailsListAdapter.OnActionClickListener onActionClickListener = new DetailsListAdapter.OnActionClickListener() {
        @Override
        public void onActionClick(String label) {
            Toast.makeText(DeviceDetailsActivity.this, "TODO clicked " + label, Toast.LENGTH_SHORT).show();
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

        MockZettaService.getDetails(new MockZettaService.Callback() {
            @Override
            public void on(List<ListItem> listItems) {
                adapter.updateAll(listItems);
            }
        });
    }
}
