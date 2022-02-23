package io.rolsdorph.fluxdroid.data.event;

import io.rolsdorph.fluxdroid.R;

public enum EventType {
    SystemStartup(R.string.evt_system_startup, Intent.ACTION_BOOT_COMPLETED),
    OutgoingCall(R.string.evt_outgoing_call, Intent.ACTION_NEW_OUTGOING_CALL),
    IncomingText(R.string.evt_incoming_text, Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

    private final int configKey;

    EventType(int configKey) {
        this.configKey = configKey;
    }

    public int getConfigKey() {
        return configKey;
    }
}
