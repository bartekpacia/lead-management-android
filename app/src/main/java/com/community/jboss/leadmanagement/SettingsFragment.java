package com.community.jboss.leadmanagement;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.community.jboss.leadmanagement.main.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);

        if (getActivity() != null) {
            Activity mActivity = getActivity();
            final SharedPreferences sharedPref = mActivity.getPreferences(Context.MODE_PRIVATE);
            final String currentServer = sharedPref.getString(getString(R.string.saved_server_ip), "https://github.com/jboss-outreach");

            final EditTextPreference mPreference = (EditTextPreference) findPreference("server_location");
            final SwitchPreference mToggleMode = (SwitchPreference) findPreference("dark_theme");
            mToggleMode.setOnPreferenceChangeListener((preference, newValue) -> {
                mActivity.startActivity(new Intent(mActivity, MainActivity.class));
                mActivity.finish();
                return true;
            });
            mPreference.setSummary(currentServer);
            mPreference.setText(currentServer);


        }
    }
}
