package pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.service;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by surecase on 17/07/14.
 */
public class BeaconTrackServiceSettings {

    private static final String KEY = "BeaconTrackServiceSettings-v1";
    private static final String ALWAYS_IN_BACKGROUND = "AlwaysInBackground";
    private static final String START_ON_BOOT = "StartOnBoot";

    public static void setAlwaysWorkInBackground(Context context, Boolean status) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(ALWAYS_IN_BACKGROUND, status).commit();
    }

    public static boolean shouldAlwaysWorkInBackground(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return prefs.getBoolean(ALWAYS_IN_BACKGROUND, false);
    }

    public static void setStartOnDeviceBoot(Context context, Boolean status) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(START_ON_BOOT, status).commit();
    }

    public static boolean shouldStartOnDeviceBoot(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return prefs.getBoolean(START_ON_BOOT, false);
    }

}
