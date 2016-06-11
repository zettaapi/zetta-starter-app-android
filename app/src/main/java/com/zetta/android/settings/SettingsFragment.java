package com.zetta.android.settings;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.novoda.notils.logger.simple.Log;
import com.zetta.android.BuildConfig;
import com.zetta.android.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsService settingsService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsService = new SettingsService(SdkProperties.newInstance(getContext()));
        addPreferencesFromResource(R.xml.preferences_settings);

        Preference appVersionPref = findPreference(getString(R.string.key_app_version));
        appVersionPref.setSummary(BuildConfig.VERSION_NAME);

        findPreference(getString(R.string.key_api_url_with_history))
            .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object url) {
                    settingsService.setRoot((String.valueOf(url)));
                    return true;
                }
            });
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (preference instanceof EditTextWithHistoryPreference) {
            DialogFragment f = ApiUrlPreferenceDialogFragment.newInstance(preference.getKey());
            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(), ApiUrlPreferenceDialogFragment.TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //no-op
    }
}
