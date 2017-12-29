package com.example.a2017_09_13.sapi_news_2017.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;

/**
 * Created by 2017-09-13 on 12/17/2017.
 */

public class permissions {
    public static final int PERMISSION_KEY = 254;
    public static final String permissions[] = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public static void permissionRequest(Context context, String permissions[], int PERMISSION_KEY) {
        ActivityCompat.requestPermissions((Activity) context, permissions, PERMISSION_KEY);
    }
}
