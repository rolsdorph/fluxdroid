package io.rolsdorph.fluxdroid;

import android.util.Log;

public enum InfluxVersion {
    Influx1X, Influx2X, Unknown;

    static InfluxVersion fromString(String s) {
        if (s == null) {
            return Unknown;
        } else if (s.equals("influx1")) {
            return Influx1X;
        } else if (s.equals("influx2")) {
            return Influx2X;
        } else {
            Log.e("InfluxVersion", "Unknown Influx version: " + s);
            return Unknown;
        }
    }
}
