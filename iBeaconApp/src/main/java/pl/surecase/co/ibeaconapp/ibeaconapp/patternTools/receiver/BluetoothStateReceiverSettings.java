package pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.receiver;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by surecase on 17/07/14.
 */
public class BluetoothStateReceiverSettings {

    private static final String KEY = "BluetoothStateReceiverSettings-v1";
    private static final String SHOULD_NOTIFY = "SHOULD_NOTIFY";

    public static void setShowNotificationAboutSateChange(Context context, Boolean status) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(SHOULD_NOTIFY, status).commit();
    }

    public static boolean isNotificationDisplayEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return prefs.getBoolean(SHOULD_NOTIFY, false);
    }

}
