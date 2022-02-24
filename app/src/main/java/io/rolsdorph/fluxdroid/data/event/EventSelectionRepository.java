package io.rolsdorph.fluxdroid.data.event;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public final class EventSelectionRepository {

    private final SharedPreferences sharedPreferences;

    public EventSelectionRepository(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isSelected(EventType eventType) {
        return sharedPreferences.getBoolean(eventType.getConfigKey(), false);
    }

}
