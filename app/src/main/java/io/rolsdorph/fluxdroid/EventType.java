package io.rolsdorph.fluxdroid;

public enum EventType {
    SystemStartup(R.string.evt_system_startup),
    IncomingCall(R.string.evt_incoming_call),
    OutgoingCall(R.string.evt_outgoing_call),
    IncomingText(R.string.evt_incoming_text);

    private final int configKey;

    EventType(int configKey) {
        this.configKey = configKey;
    }

    public int getConfigKey() {
        return configKey;
    }
}
