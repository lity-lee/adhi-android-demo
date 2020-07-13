package com.woqubo.phonelive.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Util {
    public static void requestPermission(Activity mContext, String... permissions) {
        if (!isPermissionGranted(mContext,permissions)) {
            ActivityCompat.requestPermissions(mContext, permissions, 800);
        }
    }

    private static boolean isPermissionGranted(Activity mContext,String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
