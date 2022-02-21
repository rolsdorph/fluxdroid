package io.rolsdorph.fluxdroid;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SinkConfigFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SinkConfigFragment";

    EditTextPreference influxUsername;
    EditTextPreference influxPassword;
    EditTextPreference influxToken;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.sink_config, rootKey);

        influxUsername = findPreference("influx_username");
        influxPassword = findPreference("influx_password");
        influxToken = findPreference("influx_token");

        // Only show the relevant auth field for the selected Influx version
        ListPreference influxVersion = findPreference("influx_version");
        setInfluxVersion(InfluxVersion.fromString(influxVersion.getValue()));
        influxVersion.setOnPreferenceChangeListener((Preference p, Object newValue) -> {
            setInfluxVersion(InfluxVersion.fromString((String) newValue));
            return true;
        });

        // Somehow it's not possible to set this through the XML...
        EditTextPreference influxPort = findPreference("influx_port");
        influxPort.setOnBindEditTextListener((EditText e) -> e.setInputType(InputType.TYPE_CLASS_NUMBER));
    }

    private void setInfluxVersion(InfluxVersion influxVersion) {
        if (influxVersion == InfluxVersion.Influx1X) {
            influxUsername.setEnabled(true);
            influxPassword.setEnabled(true);
            influxToken.setEnabled(false);
        } else if (influxVersion == InfluxVersion.Influx2X) {
            influxUsername.setEnabled(false);
            influxPassword.setEnabled(false);
            influxToken.setEnabled(true);
        } else {
            Log.e(TAG, "Unexpected influx version: " + influxVersion);
        }
    }
}
