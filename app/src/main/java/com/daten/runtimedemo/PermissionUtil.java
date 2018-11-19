package com.daten.runtimedemo;

import android.content.pm.PackageManager;

class PermissionUtil {

    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
