package io.rolsdorph.fluxdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.rolsdorph.fluxdroid.data.event.EventSelectionRepository;
import io.rolsdorph.fluxdroid.data.event.EventType;

public class SystemEventReceiver extends BroadcastReceiver {
    private static final String TAG = "SystemEventReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String eventName = intent.getAction();
        if (eventName == null) {
            Log.e(TAG, "Missing event name");
        } else {
            EventType eventType = EventType.forSystemEvent(eventName);
            if (eventType == null) {
                Log.e(TAG, "Unexpected event: " + eventName);
            } else {
                sendEvent(context, eventType);
            }
        }
    }

    private void sendEvent(Context context, EventType eventType) {
        boolean eventIsSelected = new EventSelectionRepository(context).isSelected(eventType);

        if (eventIsSelected) {
            Log.d(TAG, "Sending event: " + eventType);
            // Schedule here
        } else {
            Log.d(TAG, "Skipping unselected event: " + eventType);
        }
    }
}
