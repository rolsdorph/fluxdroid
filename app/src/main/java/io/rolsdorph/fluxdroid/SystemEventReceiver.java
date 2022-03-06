package io.rolsdorph.fluxdroid;

import static io.rolsdorph.fluxdroid.SendEventMetricWorker.DATA_EVENT_NAME;
import static io.rolsdorph.fluxdroid.SendEventMetricWorker.DATA_TIMESTAMP;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.time.Instant;

import io.rolsdorph.fluxdroid.data.event.EventSelectionRepository;
import io.rolsdorph.fluxdroid.data.event.EventType;

public class SystemEventReceiver extends BroadcastReceiver {
    private static final String TAG = "SystemEventReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String eventName = intent.getAction();
        if (eventName != null) {
            EventType eventType = EventType.forSystemEvent(eventName);
            if (eventType != null) {
                sendEvent(context, eventType);
            }
        }
    }

    private void sendEvent(Context context, EventType eventType) {
        boolean eventIsSelected = new EventSelectionRepository(context).isSelected(eventType);

        if (eventIsSelected) {
            WorkRequest workRequest = new OneTimeWorkRequest.Builder(SendEventMetricWorker.class)
                    .setConstraints(new Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .setRequiresBatteryNotLow(true) // TODO: make this configurable
                            .build()
                    )
                    .setInputData(new Data.Builder()
                            .putString(DATA_EVENT_NAME, eventType.getEventKey())
                            .putLong(DATA_TIMESTAMP, Instant.now().toEpochMilli())
                            .build()
                    )
                    .build();

            WorkManager.getInstance(context).enqueue(workRequest);
        }
    }
}
