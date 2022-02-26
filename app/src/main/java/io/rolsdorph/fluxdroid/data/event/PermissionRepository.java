package io.rolsdorph.fluxdroid.data.event;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public final class PermissionRepository {
    private static final String PERMA_DISMISSED = "perma_dismissed_permissions_prompt";
    private static final String DENIED_SUFFIX = "DENIED";

    private final SharedPreferences sharedPreferences;

    public PermissionRepository(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean hasBeenDenied(String permissionKey) {
        return sharedPreferences.getBoolean(permissionDeniedKey(permissionKey), false);
    }

    public void permissionDenied(String permissionKey) {
        sharedPreferences.edit().putBoolean(permissionDeniedKey(permissionKey), true).apply();
    }

    public void dismissPermissionPrompt() {
        sharedPreferences.edit().putBoolean(PERMA_DISMISSED, true).apply();
    }

    public boolean hasPermaDismissedPermissionsPrompt() {
        return sharedPreferences.getBoolean(PERMA_DISMISSED, false);
    }

    private static String permissionDeniedKey(String permissionKey) {
        return permissionKey + DENIED_SUFFIX;
    }
}
