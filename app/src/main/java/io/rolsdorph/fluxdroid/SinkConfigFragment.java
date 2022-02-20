package io.rolsdorph.fluxdroid;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SinkConfigFragment extends PreferenceFragmentCompat {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.sink_config, rootKey);


        ListPreference influxVersion = findPreference("influx_version");
        EditTextPreference influxUsername = findPreference("influx_username");
        EditTextPreference influxPassword = findPreference("influx_password");
        EditTextPreference influxToken = findPreference("influx_token");

        influxVersion.setOnPreferenceChangeListener((Preference p, Object newValue) -> {
            if (newValue.toString().equals("influx1")) {
                influxUsername.setEnabled(true);
                influxPassword.setEnabled(true);
                influxToken.setEnabled(false);
            } else {
                influxUsername.setEnabled(false);
                influxPassword.setEnabled(false);
                influxToken.setEnabled(true);
            }

            return true;
        });
    }
}
