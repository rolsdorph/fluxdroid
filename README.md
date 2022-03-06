# Fluxdroid

Fluxdroid is a simple Android application for sending phone events to InfluxDB.
It currently supports three types of events (system startup, outgoing phone call and incoming SMS).

The events are received using the [Broadcasts](https://developer.android.com/guide/components/broadcasts) functionality.

Only the event names are currently sent to Influx, so the permission set (which includes reading all SMS/call data) is quite overkill.
It would be cool to either restrict our permissions somehow, or to start sending more metadata to Influx.

## Future development ideas

 - Adding support for more events (broadcast-based ones should be very straight forward). The Bluetooth ones, for example, could be cool!
 - Making it possible to send additional event data (such as the phone number of an outgoing call) to events
 - Sending non-broadcast data (gathering and shipping "daily screen time" statistics, for example)
