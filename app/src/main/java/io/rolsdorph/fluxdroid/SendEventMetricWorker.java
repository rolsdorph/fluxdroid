package io.rolsdorph.fluxdroid;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.Optional;

import io.reactivex.rxjava3.core.Completable;
import io.rolsdorph.fluxdroid.data.db.ResultType;
import io.rolsdorph.fluxdroid.data.db.SentEvent;
import io.rolsdorph.fluxdroid.data.event.EventRepository;
import io.rolsdorph.fluxdroid.data.event.EventType;
import io.rolsdorph.fluxdroid.data.sink.InfluxConfig;
import io.rolsdorph.fluxdroid.data.sink.SinkConfigRepository;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendEventMetricWorker extends Worker {
    private static final String TAG = "SendEventMetricWorker";
    private static final MediaType TEXT = MediaType.parse("text/plain");
    private static final String EVENT_SOURCE = "fluxdroid";

    public static final String DATA_EVENT_NAME = "EVENT_NAME";
    public static final String DATA_TIMESTAMP = "TIMESTAMP";

    private final EventRepository eventRepository;
    private final SinkConfigRepository sinkConfigRepository;

    public SendEventMetricWorker(@NonNull Context context,
                                 @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.eventRepository = new EventRepository(context);
        this.sinkConfigRepository = new SinkConfigRepository(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Pair<EventType, Instant> parsedInput;
        try {
            parsedInput = parseInputData(getInputData());
        } catch (IllegalArgumentException ex) {
            return Result.failure();
        }

        EventType eventType = parsedInput.first;
        Instant timestamp = parsedInput.second;

        Optional<InfluxConfig> influxConfig = sinkConfigRepository.getInfluxConfig();
        if (influxConfig.isPresent()) {
            OkHttpClient okHttpClient = new OkHttpClient();

            ResultType resultType;
            Integer statusCode = null;
            try {
                Request request = writeRequest(eventType, timestamp, influxConfig.get());
                Response response = okHttpClient.newCall(request).execute();
                statusCode = response.code();

                if (response.isSuccessful()) {
                    resultType = ResultType.Success;
                } else {
                    // TODO: consider parsing the headers (x-influxdb-error) or response body here
                    resultType = ResultType.UnexpectedResponseCode;
                }
            } catch (IOException e) {
                resultType = ResultType.UnknownError;
            }

            storeWriteEvent(eventType, resultType, statusCode).blockingAwait();
            return resultType.isSuccess() ? Result.success() : Result.failure();
        } else {
            return Result.success();
        }
    }

    private static Pair<EventType, Instant> parseInputData(Data data) {
        String eventName = data.getString(DATA_EVENT_NAME);
        if (eventName == null) {
            throw new IllegalArgumentException("Missing event name");
        }
        EventType eventType = EventType.forSystemEvent(eventName);
        if (eventType == null) {
            throw new IllegalArgumentException("Unknown event: " + eventName);
        }

        Instant timestamp;
        try {
            timestamp = Instant.ofEpochMilli(data.getLong(DATA_TIMESTAMP, -1));
        } catch (DateTimeException exception) {
            timestamp = Instant.now();
        }

        return Pair.create(eventType, timestamp);
    }

    private Completable storeWriteEvent(@Nullable EventType eventType,
                                        ResultType resultType,
                                        Integer statusCode) {
        SentEvent sentEvent = new SentEvent(eventType, Instant.now(), resultType, statusCode);
        return eventRepository.writeEvent(sentEvent);
    }

    private static HttpUrl writeUrlFor(InfluxConfig config) {
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(config.useTLS() ? "https" : "http")
                .host(config.getHost())
                .port(config.getPort())
                .addPathSegment("write")
                .addQueryParameter("db", config.getDatabase())
                .addQueryParameter("precision", "ms");
        config.getRetentionPolicy().ifPresent(rp -> builder.addQueryParameter("rp", rp));
        return builder.build();
    }

    private static Request writeRequest(EventType eventType,
                                        Instant timestamp,
                                        InfluxConfig config) {

        RequestBody requestBody = RequestBody.create(
                toLineProtocol(eventType, timestamp, config),
                TEXT
        );
        return new Request.Builder()
                .url(writeUrlFor(config))
                .post(requestBody)
                .header("Authorization", config.getInfluxAuth().getInfluxAuthHeader())
                .build();
    }

    private static String toLineProtocol(EventType eventType,
                                         Instant timestamp,
                                         InfluxConfig influxConfig) {
        StringBuilder sb = new StringBuilder();

        // Measurement
        sb.append(influxConfig.getMeasurement());
        // Tags
        sb.append(",event=").append(eventType.getConfigKey());
        influxConfig.getDeviceId().ifPresent(id -> sb.append(",device_id=").append(escapeSpaces(id)));
        // Fields
        sb.append(" ");
        sb.append("source=\"" + EVENT_SOURCE + "\""); // Every point must have at least one field
        // Timestamp
        sb.append(" ");
        sb.append(timestamp.toEpochMilli());

        return sb.toString();
    }

    private static String escapeSpaces(String original) {
        return original.replace(" ", "\\ ");
    }
}
