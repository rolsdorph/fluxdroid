package io.rolsdorph.fluxdroid.data.db;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

import io.rolsdorph.fluxdroid.data.event.EventType;

@Entity(tableName = "sent_events")
public class SentEvent {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "event_type")
    public EventType eventType;

    @ColumnInfo(name = "received_at")
    public Instant receivedAt;

    @ColumnInfo(name = "result_type")
    public ResultType resultType;

    @ColumnInfo(name = "http_response_code")
    public Integer httpResponseCode;

    public SentEvent(@Nullable EventType eventType, Instant receivedAt, ResultType resultType, @Nullable Integer httpResponseCode) {
        this.eventType = eventType;
        this.receivedAt = receivedAt;
        this.resultType = resultType;
        this.httpResponseCode = httpResponseCode;
    }
}
