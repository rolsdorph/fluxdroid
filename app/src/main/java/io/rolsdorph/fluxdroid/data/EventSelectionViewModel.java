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

    private MutableLiveData<List<EventType>> eventsMissingPermission;
    private EventSelectionRepository eventSelectionRepository;

    public EventSelectionViewModel(@NonNull Application application) {
        super(application);
        eventSelectionRepository = new EventSelectionRepository(application);
    }

    public LiveData<Integer> getSubscribedEventCount() {
        return Transformations.map(getEventsMissingPermission(), (List<EventType> eventsMissing) -> {
            int count = 0;
            for (EventType eventType : EventType.values()) {
                if (eventSelectionRepository.isSelected(eventType) && !eventsMissing.contains(eventType)) {
                    count++;
                }
            }
            return count;
        });
    }

    public LiveData<List<EventType>> getEventsMissingPermission() {
        if (eventsMissingPermission == null) {
            eventsMissingPermission = new MutableLiveData<>();
            loadMissingPermissions();
        }

        return eventsMissingPermission;
    }

    public LiveData<Boolean> hasAllPermissions() {
        return Transformations.map(eventsMissingPermission, List::isEmpty);
    }

    public void onPermissionsChange(Map<String, Boolean> changes) {
        loadMissingPermissions();
    }

    private void loadMissingPermissions() {
        List<EventType> missingPermissions = new ArrayList<>();
        for (EventType eventType : EventType.values()) {
            if (eventType.getPermissionKey().isPresent() && !hasPermission(eventType.getPermissionKey().get())) {
                missingPermissions.add(eventType);
            }
        }
        eventsMissingPermission.setValue(missingPermissions);
    }

    private boolean hasPermission(String permissionKey) {
        return getApplication().checkSelfPermission(permissionKey) == PackageManager.PERMISSION_GRANTED;
    }
}
