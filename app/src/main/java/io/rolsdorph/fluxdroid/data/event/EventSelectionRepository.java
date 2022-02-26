package io.rolsdorph.fluxdroid.data.event;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public final class EventSelectionRepository {

    private final SharedPreferences sharedPreferences;

    public EventSelectionRepository(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isSelected(EventType eventType) {
        return sharedPreferences.getBoolean(eventType.getConfigKey(), false);
    }

    public Set<EventType> getSubscribedEvents() {
        return Arrays.stream(EventType.values()).filter(this::isSelected).collect(Collectors.toSet());
    }

}
