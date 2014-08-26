package pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.radiusnetworks.ibeacon.Region;

/**
 * Created by surecase on 04/06/14.
 */
public class BeaconTrackServiceHelper {

    static public void addRegion(Context context, Region region) {
        String uniqueId = region.getUniqueId();
        String UUID = region.getProximityUuid();
        Integer major = region.getMajor();
        Integer minor = region.getMinor();

        Intent intent = new Intent(BeaconTrackService.ACTION_ADD_REGION);
        intent.putExtra("uniqueId", uniqueId);
        intent.putExtra("UUID", UUID);
        intent.putExtra("major", major);
        intent.putExtra("minor", minor);

        context.sendBroadcast(intent);
    }

    static public void removeRegion(Context context, Region region) {
        String uniqueId = region.getUniqueId();
        String UUID = region.getProximityUuid();
        Integer major = region.getMajor();
        Integer minor = region.getMinor();

        Intent intent = new Intent(BeaconTrackService.ACTION_REMOVE_REGION);
        intent.putExtra("uniqueId", uniqueId);
        intent.putExtra("UUID", UUID);
        intent.putExtra("major", major);
        intent.putExtra("minor", minor);

        context.sendBroadcast(intent);
    }

    static public void rescheduleMonitoring(Context context) {
        Intent intent = new Intent(BeaconTrackService.ACTION_RESCHEDULE_MONITORING);
        context.sendBroadcast(intent);
    }

    static public void stopMonitoring(Context context) {
        Intent intent = new Intent(BeaconTrackService.ACTION_STOP_MONITORING);
        context.sendBroadcast(intent);
    }

    static public void rescheduleRanging(Context context) {
        Intent intent = new Intent(BeaconTrackService.ACTION_RESCHEDULE_RANGING);
        context.sendBroadcast(intent);
    }

    static public void stopRanging(Context context) {
        Intent intent = new Intent(BeaconTrackService.ACTION_STOP_RANGING);
        context.sendBroadcast(intent);
    }


    static public void restartBeaconTrackSerivce(Application application) {
        Intent intent = new Intent(application, BeaconTrackService.class);
        application.stopService(intent);
        application.startService(intent);
    }

    static public void stopBeaconTrackSerivce(Application application) {
        Intent intent = new Intent(application, BeaconTrackService.class);
        application.stopService(intent);;
    }

    static public void setBackgroundScanPeriod(Context context, long milis) {
        Intent intent = new Intent(BeaconTrackService.ACTION_SET_BACKGROUND_SCAN_PERIOD);
        intent.putExtra("milis", milis);
        context.sendBroadcast(intent);
    }

    static public void setForegroundScanPeriod(Context context, long milis) {
        Intent intent = new Intent(BeaconTrackService.ACTION_SET_FOREGROUND_SCAN_PERIOD);
        intent.putExtra("milis", milis);
        context.sendBroadcast(intent);
    }

}
