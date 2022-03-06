package io.rolsdorph.fluxdroid.data.sink;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

public final class SinkConfigRepository {
    private static final String TAG = "SinkConfigRepository";

    private final SharedPreferences sharedPreferences;

    public SinkConfigRepository(Context context) {
        sharedPreferences = createSinkSharedPreferences(context);
    }

    public static SharedPreferences createSinkSharedPreferences(Context context) {
        try {
            return EncryptedSharedPreferences.create(
                    "secret_shared_refs",
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Failed to set up encrypted preferences");
        }
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
        boolean useTLS = sharedPreferences.getBoolean("influx_tls", true);
        String retention = getNonEmptyConfig("influx_retention");
        String measurement = getNonEmptyConfig("influx_measurement");
        String deviceId = getNonEmptyConfig("influx_device_id");

        if (host != null && port != null && db != null && measurement != null) {
            return Optional.of(new InfluxConfig(host, port, useTLS, influxAuth, retention, db, measurement, deviceId));
        } else {
            return Optional.empty();
        }
    }

    private String getNonEmptyConfig(String key) {
        String value = sharedPreferences.getString(key, null);
        if (value == null) {
            return null;
        } else if (value.isEmpty()) {
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
                return null;
            }
        }
    }

    private Optional<InfluxAuth> buildInflux1Auth() {
        String username = sharedPreferences.getString("influx_username", null);
        String password = sharedPreferences.getString("influx_password", null);
        if (username != null && password != null) {
            return Optional.of(new InfluxAuth.UsernamePassword(username, password));
        } else {
            return Optional.empty();
        }
    }

    private Optional<InfluxAuth> buildInflux2Auth() {
        String token = sharedPreferences.getString("influx_token", null);
        if (token != null) {
            return Optional.of(new InfluxAuth.Token(token));
        } else {
            return Optional.empty();
        }
    }
}
