package io.rolsdorph.fluxdroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;

import io.rolsdorph.fluxdroid.data.event.EventRepository;
import io.rolsdorph.fluxdroid.data.event.EventSelectionRepository;
import io.rolsdorph.fluxdroid.data.sink.SinkConfigRepository;

public class OverviewFragment extends Fragment {
    public OverviewFragment() {
        super(R.layout.overview_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SinkConfigRepository sinkConfigRepository = new SinkConfigRepository(getContext());
        EventSelectionRepository eventSelectionRepository = new EventSelectionRepository(getContext());
        EventRepository eventRepository = new EventRepository(getContext());

        // Total number of events
        TextView eventCountNumber = view.findViewById(R.id.numEventsSentCount);
        TextView eventCountText = view.findViewById(R.id.numEventsSentText);
        LiveData<Integer> numSentEvents = eventRepository.getTotalSuccessCount();
        numSentEvents.observe(getViewLifecycleOwner(), (Integer newCount) -> {
            eventCountNumber.setText(String.valueOf(newCount));
            eventCountText.setText(getResources().getQuantityString(R.plurals.num_events_sent, newCount));
        });

        // Selected events
        int numSelectedEvents = eventSelectionRepository.getEventCount();
        TextView selectedEventsCountText = view.findViewById(R.id.selectedEventsCountText);
        selectedEventsCountText.setText(getResources().getQuantityString(R.plurals.num_events, numSelectedEvents, numSelectedEvents));
        view.findViewById(R.id.btnConfigureEvents).setOnClickListener(b -> Navigation.findNavController(view).navigate(R.id.action_overviewFragment_to_eventSelectionFragment));

        // Sink configuration
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
        view.findViewById(R.id.btnConfigureSink).setOnClickListener(b -> Navigation.findNavController(view).navigate(R.id.action_overviewFragment_to_sinkConfigFragment));
    }
}
