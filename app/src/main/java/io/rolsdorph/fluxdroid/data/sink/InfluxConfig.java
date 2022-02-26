package io.rolsdorph.fluxdroid.data.sink;

public final class InfluxConfig {
    private final String host;
    private final int port;
    private final InfluxAuth influxAuth;
    private final String retentionPolicy;
    private final String database;
    private final String measurement;

    public InfluxConfig(String host,
                        int port,
                        InfluxAuth influxAuth,
                        String retentionPolicy,
                        String database,
                        String measurement) {
        this.host = host;
        this.port = port;
        this.influxAuth = influxAuth;
        this.retentionPolicy = retentionPolicy;
        this.database = database;
        this.measurement = measurement;
    }

    public static InfluxConfig v1(String host, int port, String username, String password, String retentionPolicy, String database, String measurement) {
        return new InfluxConfig(host, port, new InfluxAuth.UsernamePassword(username, password), retentionPolicy, database, measurement);
    }

    public static InfluxConfig v2(String host, int port, String token, String retentionPolicy, String database, String measurement) {
        return new InfluxConfig(host, port, new InfluxAuth.Token(token), retentionPolicy, database, measurement);
    }
}
