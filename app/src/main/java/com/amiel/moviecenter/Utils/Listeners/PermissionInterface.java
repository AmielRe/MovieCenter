package com.amiel.moviecenter.Utils.Listeners;

import java.util.Map;

public interface PermissionInterface {
    void onGranted(Map<String, Boolean> isGranted);
}
