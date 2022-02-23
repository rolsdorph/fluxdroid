package io.rolsdorph.fluxdroid.data.event;

import android.Manifest;
import android.content.Intent;
import android.provider.Telephony;

import io.rolsdorph.fluxdroid.R;

public enum EventType {
    SystemStartup("evt_system_startup", Intent.ACTION_BOOT_COMPLETED),
    OutgoingCall("evt_outgoing_call", Intent.ACTION_NEW_OUTGOING_CALL),
    IncomingText("evt_incoming_text", Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

    private final String configKey;
    private final String eventKey;

    EventType(String configKey, String eventKey) {
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

    public String getConfigKey() {
        return configKey;
    }

    public String getEventKey() {
        return eventKey;
    }
}
