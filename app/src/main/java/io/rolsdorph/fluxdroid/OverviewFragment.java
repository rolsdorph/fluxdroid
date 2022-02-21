package io.rolsdorph.fluxdroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

        SinkConfigRepository sinkConfigRepository = new SinkConfigRepository(getContext());

        view.findViewById(R.id.btnConfigureEvents).setOnClickListener(b -> Navigation.findNavController(view).navigate(R.id.action_overviewFragment_to_eventSelectionFragment));
        view.findViewById(R.id.btnConfigureSink).setOnClickListener(b -> Navigation.findNavController(view).navigate(R.id.action_overviewFragment_to_sinkConfigFragment));

        ImageView imageView = view.findViewById(R.id.sinkIcon);
        View sinkTextConfigured = view.findViewById(R.id.sinkTextConfigured);
        View sinkTextNotConfigured = view.findViewById(R.id.sinkTextNotConfigured);

        if (sinkConfigRepository.cloudConfigured()) {
            imageView.setImageResource(R.drawable.ic_baseline_cloud_queue_32);
            sinkTextConfigured.setVisibility(View.VISIBLE);
            sinkTextNotConfigured.setVisibility(View.GONE);
        } else {
            imageView.setImageResource(R.drawable.ic_baseline_cloud_off_32);
            sinkTextConfigured.setVisibility(View.GONE);
            sinkTextNotConfigured.setVisibility(View.VISIBLE);
        }
    }
}
