package com.zetta.android.settings.licenses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.zetta.android.R;

import java.util.ArrayList;
import java.util.List;

public class OpenSourceLicensesActivity extends AppCompatActivity {

    private static final List<License> LICENSES = new ArrayList<>();

    static {
        LICENSES.add(new License("Android", "Google Inc. and the Open Handset Alliance",
                                 "Android is licensed under the Apache License, Version 2.0", "https://android.googlesource.com/"
        ));
        LICENSES.add(new License("Android Support Library", "Google Inc. and the Open Handset Alliance",
                                 "Android is licensed under the Apache License, Version 2.0", "https://android.googlesource.com/"
        ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_source_licenses_activity);

        RecyclerView openSourceLicensesList = (RecyclerView) findViewById(R.id.open_source_licenses_list);
        openSourceLicensesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LicenseAdapter licenseAdapter = new LicenseAdapter(layoutInflater);
        licenseAdapter.addAll(LICENSES);
        openSourceLicensesList.setAdapter(licenseAdapter);
    }

}
