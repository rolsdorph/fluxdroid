package io.rolsdorph.fluxdroid;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class EventSelectionFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.event_selection, rootKey);
    }
}
