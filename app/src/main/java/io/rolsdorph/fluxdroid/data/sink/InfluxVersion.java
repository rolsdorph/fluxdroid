package io.rolsdorph.fluxdroid.data.sink;

import android.util.Log;

public enum InfluxVersion {
    Influx1X, Influx2X, Unknown;

    public static InfluxVersion fromString(String s) {
        if (s == null) {
            return Unknown;
        } else if (s.equals("influx1")) {
            return Influx1X;
        } else if (s.equals("influx2")) {
            return Influx2X;
        } else {
            return Unknown;
        }
    }
}
