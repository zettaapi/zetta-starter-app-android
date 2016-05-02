package com.zetta.android.browse;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zetta.android.ImageLoader;
import com.zetta.android.R;
import com.zetta.android.settings.SettingsActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DeviceListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.device_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DeviceListAdapter adapter = new DeviceListAdapter(new ImageLoader());
        List<ListItem> items;
        try {
            items = Arrays.asList(
                new ListItem.ServerListItem(Color.parseColor("#0000ff"), "bangalor"),
                new ListItem.DeviceListItem("Door", "closed", new URL("http://www.zettaapi.org/icons/door-closed.png")),
                new ListItem.DeviceListItem("Photocell", "ready", new URL("http://www.zettaapi.org/icons/photocell-ready.png")),
                new ListItem.DeviceListItem("Security System", "disarmed", new URL("http://www.zettaapi.org/icons/security-disarmed.png")),
                new ListItem.DeviceListItem("Window", "closed", new URL("http://www.zettaapi.org/icons/window-closed.png")),
                new ListItem.ServerListItem(Color.parseColor("#dd33ff"), "neworleans"),
                new ListItem.DeviceListItem("Motion", "no-motion", new URL("http://www.zettaapi.org/icons/motion-no-motion.png")),
                new ListItem.DeviceListItem("Thermometer", "ready", new URL("http://www.zettaapi.org/icons/thermometer-ready.png")),
                new ListItem.DeviceListItem("Camera", "ready", new URL("http://www.zettaapi.org/public/demo/detroit.jpg"))
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        adapter.updateAll(items);
        recyclerView.setAdapter(adapter);

    }

    // TODO hide toolbar on scroll https://guides.codepath.com/android/Using-the-App-ToolBar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
