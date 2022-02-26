package io.rolsdorph.fluxdroid.data.sink;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.Optional;

public final class SinkConfigRepository {
    private static final String TAG = "SinkConfigRepository";

    private final SharedPreferences sharedPreferences;

    public SinkConfigRepository(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean cloudConfigured() {
        return getInfluxConfig().isPresent();
    }

    public Optional<InfluxConfig> getInfluxConfig() {
        Optional<InfluxAuth> influxAuth = buildInfluxAuth();
        return influxAuth.flatMap(this::buildInfluxConfig);
    }

    public void clearPreference(String settingsKey) {
        sharedPreferences.edit().remove(settingsKey).apply();
    }

    private Optional<InfluxAuth> buildInfluxAuth() {
        String influxVersion = sharedPreferences.getString("influx_version", null);
        Log.i(TAG, "Influx version: " + influxVersion);
        if (influxVersion == null) {
            return Optional.empty();
        } else if (influxVersion.equals("influx1")) {
            return buildInflux1Auth();
        } else if (influxVersion.equals("influx2")) {
            return buildInflux2Auth();
        } else {
            return Optional.empty();
        }
    }

    private Optional<InfluxConfig> buildInfluxConfig(InfluxAuth influxAuth) {
        String host = getNonEmptyConfig("influx_host");
        Integer port = getPort();
        String db = getNonEmptyConfig("influx_db");
        String retention = getNonEmptyConfig("influx_retention");

        if (host != null && port != null && db != null && retention != null) {
            Log.i(TAG, "Influx fully configured!");
            return Optional.of(new InfluxConfig(host, port, influxAuth, retention, db));
        } else {
            Log.i(TAG, "Influx not fully configured. Missing fields: " +
                    ((host == null) ? "host, " : "")
                    + ((port == null) ? "port, " : "")
                    + ((db == null) ? "db, " : "")
                    + ((retention == null) ? "retention" : ""));
            return Optional.empty();
        }
    }

    private String getNonEmptyConfig(String key) {
        String value = sharedPreferences.getString(key, null);
        if (value == null) {
            Log.i(TAG, "Missing config value for " + key);
            return null;
        } else if (value.isEmpty()) {
            Log.i(TAG, "Empty config value for " + key);
            return null;
        } else {
            return value;
        }
    }

    private Integer getPort() {
        String portString = getNonEmptyConfig("influx_port");
        if (portString == null) {
            return null;
        } else {
            try {
                return Integer.valueOf(portString);
            } catch (NumberFormatException ex) {
                Log.e(TAG, "Failed to parse port number: " + portString);
                return null;
            }
        }
    }

    private Optional<InfluxAuth> buildInflux1Auth() {
        String username = sharedPreferences.getString("influx_username", null);
        String password = sharedPreferences.getString("influx_password", null);
        if (username != null && password != null) {
            Log.i(TAG, "Influx 1 auth fully configured");
            return Optional.of(new InfluxAuth.UsernamePassword(username, password));
        } else {
            Log.i(TAG, "Influx 1 auth not fully configured.");
            return Optional.empty();
        }
    }

    private Optional<InfluxAuth> buildInflux2Auth() {
        String token = sharedPreferences.getString("influx_token", null);
        if (token != null) {
            Log.i(TAG, "Influx 2 auth fully configured");
            return Optional.of(new InfluxAuth.Token(token));
        } else {
            Log.i(TAG, "Influx 2 auth not fully configured.");
            return Optional.empty();
        }
    }
}
