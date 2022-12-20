package com.amiel.moviecenter.Utils;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

public class PermissionHelper {

    public void startPermissionRequest(FragmentActivity fr, PermissionInterface permissionInterface, String... permissions) {
        ActivityResultLauncher<String[]> requestPermissionLauncher =
                fr.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissionInterface::onGranted);
        requestPermissionLauncher.launch(permissions);
    }

    public static boolean isMissingPermissions(FragmentActivity fr, String... permissions) {
        if(fr != null && permissions != null) {
            for (String permission : permissions) {
                if (fr.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
