package pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by surecase on 28/03/14.
 */
public class BeaconTrackServiceAutostart extends BroadcastReceiver {

    public void onReceive(Context arg0, Intent arg1) {
        if (!BeaconTrackServiceSettings.shouldStartOnDeviceBoot(arg0)) {
            return;
        }
        Intent intent = new Intent(arg0, BeaconTrackService.class);
        arg0.startService(intent);
    }
}