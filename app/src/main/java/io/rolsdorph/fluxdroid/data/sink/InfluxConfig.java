package io.rolsdorph.fluxdroid.data.sink;

public final class InfluxConfig {
    private final String host;
    private final int port;
    private final boolean useTLS;
    private final InfluxAuth influxAuth;
    private final String database;
    private final String measurement;

    public InfluxConfig(String host,
                        int port,
                        boolean useTLS,
                        InfluxAuth influxAuth,
                        String database,
                        String measurement) {
        this.host = host;
        this.port = port;
        this.useTLS = useTLS;
        this.influxAuth = influxAuth;
        this.database = database;
        this.measurement = measurement;
    }

    public static InfluxConfig v1(String host, int port, boolean useTLS, String username, String password, String database, String measurement) {
        return new InfluxConfig(host, port, useTLS, new InfluxAuth.UsernamePassword(username, password), database, measurement);
    }

    public static InfluxConfig v2(String host, int port, boolean useTLS, String token, String database, String measurement) {
        return new InfluxConfig(host, port, useTLS, new InfluxAuth.Token(token), database, measurement);
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

    public String getDatabase() {
        return database;
    }

    public String getMeasurement() {
        return measurement;
    }
}
