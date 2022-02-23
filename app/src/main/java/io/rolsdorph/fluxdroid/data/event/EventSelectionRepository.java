package io.rolsdorph.fluxdroid.data.event;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public final class EventSelectionRepository {

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public EventSelectionRepository(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getEventCount() {
        int eventCount = 0;
        for (EventType eventType : EventType.values()) {
            String eventKey = context.getString(eventType.getConfigKey());
            if (eventKey != null && sharedPreferences.getBoolean(eventKey, false)) {
                eventCount++;
            }
        }
        return eventCount;
    }

}
