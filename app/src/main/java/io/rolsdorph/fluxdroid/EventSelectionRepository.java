package io.rolsdorph.fluxdroid;

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
        for (SystemEvent systemEvent : SystemEvent.values()) {
            String eventKey = context.getString(systemEvent.configKey);
            if (eventKey != null && sharedPreferences.getBoolean(eventKey, false)) {
                eventCount++;
            }
        }
        return eventCount;
    }

    private enum SystemEvent {
        SystemStartup(R.string.evt_system_startup),
        IncomingCall(R.string.evt_incoming_call),
        OutgoingCall(R.string.evt_outgoing_call),
        IncomingText(R.string.evt_incoming_text);

        private final int configKey;

        SystemEvent(int configKey) {
            this.configKey = configKey;
        }
    }
}
