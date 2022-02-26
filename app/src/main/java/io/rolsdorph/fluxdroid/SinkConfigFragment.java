package io.rolsdorph.fluxdroid;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import io.rolsdorph.fluxdroid.data.sink.InfluxVersion;
import io.rolsdorph.fluxdroid.data.sink.SinkConfigRepository;

public class SinkConfigFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SinkConfigFragment";

    EditTextPreference influxUsername;
    EditTextPreference influxPassword;
    EditTextPreference influxToken;
    SinkConfigRepository sinkConfigRepository;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.sink_config, rootKey);

        sinkConfigRepository = new SinkConfigRepository(requireContext());

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

        markAsPassword(findPreference("influx_password"));
        markAsPassword(findPreference("influx_token"));
    }

    private void markAsPassword(EditTextPreference editTextPreference) {
        editTextPreference.setOnBindEditTextListener((EditText e) ->
                e.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
        editTextPreference.setSummaryProvider(new PasswordSummaryProvider());
    }

    private void setInfluxVersion(InfluxVersion influxVersion) {
        if (influxVersion == InfluxVersion.Influx1X) {
            influxUsername.setVisible(true);
            influxPassword.setVisible(true);
            clearAndHidePreference(influxToken);
        } else if (influxVersion == InfluxVersion.Influx2X) {
            clearAndHidePreference(influxUsername);
            clearAndHidePreference(influxPassword);
            influxToken.setVisible(true);
        } else {
            Log.e(TAG, "Unexpected influx version: " + influxVersion);
        }
    }

    private void clearAndHidePreference(EditTextPreference editTextPreference) {
        editTextPreference.setVisible(false);
        editTextPreference.setText(null);
        sinkConfigRepository.clearPreference(editTextPreference.getKey());
    }

    private final class PasswordSummaryProvider implements Preference.SummaryProvider<EditTextPreference> {
        @Override
        public CharSequence provideSummary(@NonNull EditTextPreference preference) {
            String password = preference.getText();
            if (password == null || password.isEmpty()) {
                return getResources().getString(R.string.pw_not_set);
            } else {
                return getResources().getString(R.string.pw_censored);
            }
        }
    }
}
