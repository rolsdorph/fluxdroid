package io.rolsdorph.fluxdroid.data.db;

import androidx.room.TypeConverter;

import java.time.Instant;

public class Converters {
    @TypeConverter
    public static Instant fromEpochMilli(Long value) {
        return value == null ? null : Instant.ofEpochMilli(value);
    }

    @TypeConverter
    public static Long instantToEpochMilli(Instant instant) {
        return instant == null ? null : instant.toEpochMilli();
    }
}
