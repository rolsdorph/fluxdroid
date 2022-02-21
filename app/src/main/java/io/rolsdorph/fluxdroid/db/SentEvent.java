package io.rolsdorph.fluxdroid.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

import io.rolsdorph.fluxdroid.EventType;

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
    public int httpResponseCode;

    public static SentEvent successful(EventType eventType, int httpResponseCode) {
        return new SentEvent(eventType, Instant.now(), ResultType.Success, httpResponseCode);
    }

    public SentEvent(EventType eventType, Instant receivedAt, ResultType resultType, int httpResponseCode) {
        this.eventType = eventType;
        this.receivedAt = receivedAt;
        this.resultType = resultType;
        this.httpResponseCode = httpResponseCode;
    }
}
