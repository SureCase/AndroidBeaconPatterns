package pl.surecase.co.ibeaconapp.ibeaconapp.example.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by surecase on 21/07/14.
 */
public class BeaconDataStore {

    public final static String ESTIMOTE_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";

    private static final String KEY = "BeaconDataStore-v1";
    private static final String BEACON_MAJOR = "BeaconMajor";
    private static final String BEACON_MINOR = "BeaconMinor";

    public static void setBeaconMajor(Context context, int major) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        prefs.edit().putInt(BEACON_MAJOR, major).commit();
    }

    public static int getBeaconMajor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return prefs.getInt(BEACON_MAJOR, 0);
    }

    public static void setBeaconMinor(Context context, int minor) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        prefs.edit().putInt(BEACON_MINOR, minor).commit();
    }

    public static int getBeaconMinor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return prefs.getInt(BEACON_MINOR, 0);
    }

}
