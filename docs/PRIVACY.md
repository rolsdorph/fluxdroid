# Privacy Policy

Fluxdroid stores your InfluxDB configuration (encrypted, on your device), and uses it to send events you've subscribed to to your InfluxDB server.

## Data collected and stored by Fluxdroid

The InfluxDB configuration data is stored on your phone using Androids [EncryptedSharedPreferences](https://developer.android.com/reference/androidx/security/crypto/EncryptedSharedPreferences)
mechanism. It is only used for transmitting the events you have selected.

## Data collected and forwarded by Fluxdroid

Fluxdroid listens for the events you have selected and notifies your configured InfluxDB target about them. The notification only contains

 - The Device ID (if you provided one in the InfluxDB config)
 - The name of the event ("evt_system_startup", "evt_outgoing_call" or "evt_incoming_text")
 - The current timestamp

That's it. No other details (such as outgoing call number, incoming text info) are currently supported.
