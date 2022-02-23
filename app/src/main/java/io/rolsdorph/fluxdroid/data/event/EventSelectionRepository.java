package io.rolsdorph.fluxdroid.data.event;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public final class EventSelectionRepository {

    private final SharedPreferences sharedPreferences;

    public EventSelectionRepository(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getEventCount() {
        int eventCount = 0;
        for (EventType eventType : EventType.values()) {
            if (sharedPreferences.getBoolean(eventType.getConfigKey(), false)) {
                eventCount++;
            }
        }
        return eventCount;
    }

    public boolean isSelected(EventType eventType) {
        return sharedPreferences.getBoolean(eventType.getConfigKey(), false);
    }

}
