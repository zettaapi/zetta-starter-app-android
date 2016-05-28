package com.zetta.android.device.events;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.zetta.android.R;

import java.util.List;

public class EventsActivity extends AppCompatActivity {

    private EventsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.toolbar_title).setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Events);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        adapter = new EventsListAdapter();
        RecyclerView deviceListWidget = (RecyclerView) findViewById(R.id.events_list);
        deviceListWidget.setAdapter(adapter);
        deviceListWidget.setHasFixedSize(true);
        deviceListWidget.setLayoutManager(new LinearLayoutManager(this));

        MockZettaService.getEvents(new MockZettaService.Callback() {
            @Override
            public void on(List<ListItem> listItems) {
                adapter.updateAll(listItems);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }
}
