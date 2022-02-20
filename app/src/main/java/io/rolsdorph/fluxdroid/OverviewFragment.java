package io.rolsdorph.fluxdroid;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class OverviewFragment extends Fragment {
    public OverviewFragment() {
        super(R.layout.overview_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnConfigureEvents).setOnClickListener(b -> Navigation.findNavController(view).navigate(R.id.action_overviewFragment_to_eventSelectionFragment));
        view.findViewById(R.id.btnConfigureSink).setOnClickListener(b -> Navigation.findNavController(view).navigate(R.id.action_overviewFragment_to_sinkConfigFragment));
    }
}
