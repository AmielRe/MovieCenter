package com.amiel.moviecenter.Utils;

import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class PermissionHelper {

    public static ActivityResultLauncher<String[]> registerForActivityResult(Fragment fr, PermissionInterface permissionInterface) {
        return fr.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissionInterface::onGranted);
    }
    public void startPermissionRequest(ActivityResultLauncher<String[]> requestPermissionLauncher, String... permissions) {
        requestPermissionLauncher.launch(permissions);
    }

    public static boolean isMissingPermissions(FragmentActivity fr, String... permissions) {
        if(fr != null && permissions != null) {
            for (String permission : permissions) {
                if (fr.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
