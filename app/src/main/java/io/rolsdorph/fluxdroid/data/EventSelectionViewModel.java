package io.rolsdorph.fluxdroid.data;

import android.app.Application;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.rolsdorph.fluxdroid.data.event.EventType;
import io.rolsdorph.fluxdroid.data.event.PermissionRepository;

public class EventSelectionViewModel extends AndroidViewModel {
    private MutableLiveData<List<Event>> events;
    private final PermissionRepository permissionRepository;

    public EventSelectionViewModel(@NonNull Application application) {
        super(application);
        permissionRepository = new PermissionRepository(application);
    }

    private MutableLiveData<Boolean> hasDismissedPermissionsPrompt;
    private MediatorLiveData<PromptState> promptState;

    public LiveData<Boolean> shouldShowPermissionsPrompt() {
        if (promptState == null) {
            promptState = new MediatorLiveData<>();
            promptState.addSource(hasDismissedPermissionsPrompt(), permaDismissed -> {
                if (permaDismissed) {
                    promptState.setValue(PromptState.permaDismissed);
                }
            });
            promptState.addSource(hasMissingPermissions(), missingPermissions -> {
                if (promptState.getValue() != PromptState.permaDismissed) {
                    if (missingPermissions) {
                        promptState.setValue(PromptState.missingPermissions);
                    } else {
                        promptState.setValue(PromptState.noMissingPermissions);
                    }
                }
            });
        }

        return Transformations.map(promptState, p -> p == PromptState.missingPermissions);
    }

    public LiveData<List<Event>> getEvents() {
        if (events == null) {
            events = new MutableLiveData<>();
            loadEvents();
        }

        return events;
    }

    public LiveData<Boolean> hasUndecidedPermissions() {
        return Transformations.map(getEvents(), currentEvents -> currentEvents.stream()
                .anyMatch(e -> e.getPermissionState() == PermissionState.undecided));
    }

    public void onPermissionsChange(Map<String, Boolean> changes) {
        for (Map.Entry<String, Boolean> permissionChange : changes.entrySet()) {
            if (!permissionChange.getValue()) {
                permissionRepository.permissionDenied(permissionChange.getKey());
            }
        }
        loadEvents();
    }

    public void onDismissPermissions() {
        permissionRepository.dismissPermissionPrompt();
        loadPermaDismiss();
    }

    private LiveData<Boolean> hasDismissedPermissionsPrompt() {
        if (hasDismissedPermissionsPrompt == null) {
            hasDismissedPermissionsPrompt = new MutableLiveData<>();
            loadPermaDismiss();
        }
        return hasDismissedPermissionsPrompt;
    }

    private LiveData<Boolean> hasMissingPermissions() {
        return Transformations.map(events, l -> l.stream().anyMatch(Event::isMissingPermission));
    }

    private void loadEvents() {
        List<Event> newEvents = new ArrayList<>();
        for (EventType eventType : EventType.values()) {
            newEvents.add(new Event(eventType, permissionState(eventType)));
        }
        events.setValue(newEvents);
    }

    private void loadPermaDismiss() {
        hasDismissedPermissionsPrompt.setValue(permissionRepository.hasPermaDismissedPermissionsPrompt());
    }

    private PermissionState permissionState(EventType eventType) {
        if (eventType.getPermissionKey().isPresent()) {
            String key = eventType.getPermissionKey().get();
            if (hasPermission(key)) {
                return PermissionState.allowed;
            } else if (permissionRepository.hasBeenDenied(key)) {
                return PermissionState.denied;
            } else {
                return PermissionState.undecided;
            }
        } else {
            return PermissionState.allowed;
        }
    }

    private boolean hasPermission(String permissionKey) {
        return getApplication().checkSelfPermission(permissionKey) == PackageManager.PERMISSION_GRANTED;
    }

    public static final class Event {
        private final EventType eventType;
        private final PermissionState permissionState;

        public Event(EventType eventType, PermissionState permissionState) {
            this.eventType = eventType;
            this.permissionState = permissionState;
        }

        public EventType getEventType() {
            return eventType;
        }

        public PermissionState getPermissionState() {
            return permissionState;
        }

        public boolean isMissingPermission() {
            return permissionState != PermissionState.allowed;
        }
    }

    public enum PromptState {
        permaDismissed, noMissingPermissions, missingPermissions
    }

    public enum PermissionState {
        allowed, denied, undecided
    }
}
