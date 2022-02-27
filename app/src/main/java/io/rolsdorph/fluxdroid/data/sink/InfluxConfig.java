package io.rolsdorph.fluxdroid.data.sink;

import androidx.annotation.Nullable;

import java.util.Optional;

public final class InfluxConfig {
    private final String host;
    private final int port;
    private final boolean useTLS;
    private final InfluxAuth influxAuth;
    private final String retentionPolicy;
    private final String database;
    private final String measurement;

    public InfluxConfig(String host,
                        int port,
                        boolean useTLS,
                        InfluxAuth influxAuth,
                        @Nullable String retentionPolicy,
                        String database,
                        String measurement) {
        this.host = host;
        this.port = port;
        this.useTLS = useTLS;
        this.influxAuth = influxAuth;
        this.retentionPolicy = retentionPolicy;
        this.database = database;
        this.measurement = measurement;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean useTLS() {
        return useTLS;
    }

    public InfluxAuth getInfluxAuth() {
        return influxAuth;
    }

    public Optional<String> getRetentionPolicy() {
        return Optional.ofNullable(retentionPolicy);
    }

    public String getDatabase() {
        return database;
    }

    public String getMeasurement() {
        return measurement;
    }
}
