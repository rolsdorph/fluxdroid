package io.rolsdorph.fluxdroid.data.event;

import android.Manifest;
import android.content.Intent;
import android.provider.Telephony;

import java.util.Optional;

import io.rolsdorph.fluxdroid.R;

public enum EventType {
    SystemStartup("evt_system_startup", Intent.ACTION_BOOT_COMPLETED, null),
    OutgoingCall("evt_outgoing_call", Intent.ACTION_NEW_OUTGOING_CALL, Manifest.permission.PROCESS_OUTGOING_CALLS),
    IncomingText("evt_incoming_text", Telephony.Sms.Intents.SMS_RECEIVED_ACTION, Manifest.permission.RECEIVE_SMS);

    private final String configKey;
    private final String eventKey;
    private final String permissionKey;

    EventType(String configKey, String eventKey, String permissionKey) {
        this.configKey = configKey;
        this.eventKey = eventKey;
        this.permissionKey = permissionKey;
    }

    public static EventType forPermission(String permission) {
        for (EventType eventType : values()) {
            if (permission.equals(eventType.permissionKey)) {
                return eventType;
            }
        }

        return null;
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

    public Optional<String> getPermissionKey() {
        return Optional.ofNullable(permissionKey);
    }
}
