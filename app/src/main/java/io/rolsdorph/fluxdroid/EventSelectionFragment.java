package io.rolsdorph.fluxdroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.List;

import io.rolsdorph.fluxdroid.data.EventSelectionViewModel;
import io.rolsdorph.fluxdroid.data.event.EventType;

public class EventSelectionFragment extends PreferenceFragmentCompat {
    private static final String TAG = "EventSelectionFragment";

    private EventSelectionViewModel viewModel;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.event_selection, rootKey);
        viewModel = new ViewModelProvider(requireActivity()).get(EventSelectionViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getEventsMissingPermission().observe(getViewLifecycleOwner(),
                (List<EventType> eventsMissingPermission) -> {
                    for (EventType missingPermission : eventsMissingPermission) {
                        SwitchPreferenceCompat preference = findPreference(missingPermission.getConfigKey());
                        if (preference == null) {
                            Log.e(TAG, "Missing preference view for " + missingPermission);
                        } else {
                            preference.setChecked(false);
                            preference.setEnabled(false);
                        }
                    }
                });
    }
}
