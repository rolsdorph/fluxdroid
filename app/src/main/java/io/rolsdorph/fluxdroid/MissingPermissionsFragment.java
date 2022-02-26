package io.rolsdorph.fluxdroid;

import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;

import io.rolsdorph.fluxdroid.data.EventSelectionViewModel;
import io.rolsdorph.fluxdroid.data.event.EventType;

public class MissingPermissionsFragment extends Fragment {

    private ActivityResultLauncher<String[]> permissionsLauncher;

    public MissingPermissionsFragment() {
        super(R.layout.missing_permissions_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventSelectionViewModel viewModel = new ViewModelProvider(requireActivity()).get(EventSelectionViewModel.class);

        permissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), viewModel::onPermissionsChange);

        View missingPermissionsPrompt = view.findViewById(R.id.missingPermissionsPrompt);
        View configurePermissionsButton = view.findViewById(R.id.configurePermissionsButton);
        View dismissButton = view.findViewById(R.id.dontShowAgainButton);

        dismissButton.setOnClickListener(v -> viewModel.onDismissPermissions());

        viewModel.shouldShowPermissionsPrompt().observe(getViewLifecycleOwner(), shouldShow -> {
            if (shouldShow) {
                missingPermissionsPrompt.setVisibility(View.VISIBLE);
            } else {
                missingPermissionsPrompt.setVisibility(View.GONE);
            }
        });

        viewModel.hasUndecidedPermissions().observe(getViewLifecycleOwner(), canRequest -> {
            if (canRequest) {
                configurePermissionsButton.setVisibility(View.VISIBLE);
            } else {
                configurePermissionsButton.setVisibility(View.GONE);
            }
        });

        String[] allEvents = Arrays.stream(EventType.values())
                .filter(e -> e.getPermissionKey().isPresent())
                .map(e -> e.getPermissionKey().get())
                .toArray(String[]::new);
        configurePermissionsButton.setOnClickListener(v -> permissionsLauncher.launch(allEvents));
    }
}
