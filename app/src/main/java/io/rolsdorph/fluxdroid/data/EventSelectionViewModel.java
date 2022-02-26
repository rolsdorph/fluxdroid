package io.rolsdorph.fluxdroid.data;

import android.app.Application;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.rolsdorph.fluxdroid.data.event.EventSelectionRepository;
import io.rolsdorph.fluxdroid.data.event.EventType;

public class EventSelectionViewModel extends AndroidViewModel {

    private MutableLiveData<List<Event>> events;

    public EventSelectionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Event>> getEvents() {
        if (events == null) {
            events = new MutableLiveData<>();
            loadEvents();
        }

        return events;
    }

    public LiveData<Boolean> hasAllPermissions() {
        return Transformations.map(events, l -> l.stream().noneMatch(Event::isMissingPermission));
    }

    public void onPermissionsChange(Map<String, Boolean> changes) {
        loadEvents();
    }

    private void loadEvents() {
        List<Event> newEvents = new ArrayList<>();
        for (EventType eventType : EventType.values()) {
            boolean missingPermission = eventType.getPermissionKey().isPresent() && !hasPermission(eventType.getPermissionKey().get());
            newEvents.add(new Event(eventType, missingPermission));
        }
        events.setValue(newEvents);
    }

    private boolean hasPermission(String permissionKey) {
        return getApplication().checkSelfPermission(permissionKey) == PackageManager.PERMISSION_GRANTED;
    }

    public static final class Event {
        private final EventType eventType;
        private final boolean missingPermission;

        public Event(EventType eventType, boolean missingPermission) {
            this.eventType = eventType;
            this.missingPermission = missingPermission;
        }

        public EventType getEventType() {
            return eventType;
        }

        public boolean isMissingPermission() {
            return missingPermission;
        }
    }
}
