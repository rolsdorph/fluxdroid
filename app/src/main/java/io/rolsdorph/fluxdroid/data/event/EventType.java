package io.rolsdorph.fluxdroid.data.event;

import android.content.Intent;
import android.provider.Telephony;

import io.rolsdorph.fluxdroid.R;

public enum EventType {
    SystemStartup(R.string.evt_system_startup, Intent.ACTION_BOOT_COMPLETED),
    OutgoingCall(R.string.evt_outgoing_call, Intent.ACTION_NEW_OUTGOING_CALL),
    IncomingText(R.string.evt_incoming_text, Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

    private final int configKey;
    private final String eventKey;

    EventType(int configKey, String eventKey) {
        this.configKey = configKey;
        this.eventKey = eventKey;
    }

    public static EventType forSystemEvent(String eventKey) {
        for (EventType eventType : values()) {
            if (eventType.eventKey.equals(eventKey)) {
                return eventType;
            }
        }

        return null;
    }

    public int getConfigKey() {
        return configKey;
    }

    public String getEventKey() {
        return eventKey;
    }
}
