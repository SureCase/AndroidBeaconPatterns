package pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableIBeacon;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableIBeaconList;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableRegion;

/**
 * Created by surecase on 28/03/14.
 */
public class BeaconTrackService extends Service implements IBeaconConsumer {

    public static final String ACTION_ADD_REGION = "ibeacon.track.service" + "add.region";
    public static final String ACTION_REMOVE_REGION = "ibeacon.track.service" + "remove.region";
    public static final String ACTION_RESCHEDULE_MONITORING = "ibeacon.track.service" + "reschedule.monitoring";
    public static final String ACTION_STOP_MONITORING = "ibeacon.track.service" + "stop.monitoring";
    public static final String ACTION_RESCHEDULE_RANGING = "ibeacon.track.service" + "reschedule.ranging";
    public static final String ACTION_STOP_RANGING = "ibeacon.track.service" + "stop.ranging";
    public static final String ACTION_SET_FOREGROUND_SCAN_PERIOD = "ibeacon.track.service" + "set.foreground.scan.peroid";
    public static final String ACTION_SET_BACKGROUND_SCAN_PERIOD = "ibeacon.track.service" + "set.background.scan.peroid";

    public static final String CALLBACK_BEACON_SERVICE_CONNECTED = "ibeacon.track.service" + "beacon.service.connected";
    public static final String CALLBACK_MONITORING_ENTER = "ibeacon.track.service" + "monitoring.enter";
    public static final String CALLBACK_MONITORING_EXIT = "ibeacon.track.service" + "monitoring.exit";
    public static final String CALLBACK_MONITORING_DETERMINE_STATE = "ibeacon.track.service" + "monitoring.determine.state";
    public static final String CALLBACK_MONITORING_RESCHEDULE_ERROR = "ibeacon.track.service" + "monitoring.start.error";
    public static final String CALLBACK_MONITORING_STOP_ERROR = "ibeacon.track.service" + "monitoring.stop.error";

    public static final String CALLBACK_RANGING_DID_RANGE = "ibeacon.track.service" + "ranging.did.range";
    public static final String CALLBACK_RANGING_RESCHEDULE_ERROR = "ibeacon.track.service" + "ranging.start.error";
    public static final String CALLBACK_RANGING_STOP_ERROR = "ibeacon.track.service" + "ranging.stop.error";

    final private List<Region> regionList = new ArrayList<Region>();

    private IBeaconManager iBeaconManager;
    private BroadcastReceiver trackServiceReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        iBeaconManager = IBeaconManager.getInstanceForApplication(this);
        iBeaconManager.bind(this);
        registerReceiver();
    }

    private void registerReceiver() {
        trackServiceReceiver = createTrackBeaconReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ADD_REGION);
        filter.addAction(ACTION_REMOVE_REGION);
        filter.addAction(ACTION_RESCHEDULE_MONITORING);
        filter.addAction(ACTION_STOP_MONITORING);
        filter.addAction(ACTION_RESCHEDULE_RANGING);
        filter.addAction(ACTION_STOP_RANGING);
        filter.addAction(ACTION_SET_BACKGROUND_SCAN_PERIOD);
        filter.addAction(ACTION_SET_FOREGROUND_SCAN_PERIOD);
        registerReceiver(trackServiceReceiver, filter);
    }

    private BroadcastReceiver createTrackBeaconReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == null) {
                    return;

                } else if (intent.getAction().equals(ACTION_ADD_REGION)) {
                    String uniqueId = intent.getExtras().getString("uniqueId", null);
                    String UUID = intent.getExtras().getString("UUID", null);
                    Integer major = (Integer) intent.getExtras().get("major");
                    Integer minor = (Integer) intent.getExtras().get("minor");

                    Region region = new Region(uniqueId, UUID, major, minor);
                    addRegion(region);

                } else if (intent.getAction().equals(ACTION_REMOVE_REGION)) {
                    String uniqueId = intent.getExtras().getString("uniqueId", null);
                    String UUID = intent.getExtras().getString("UUID", null);
                    Integer major = (Integer) intent.getExtras().get("major");
                    Integer minor = (Integer) intent.getExtras().get("minor");

                    Region region = new Region(uniqueId, UUID, major, minor);
                    removeRegion(region);

                } else if (intent.getAction().equals(ACTION_RESCHEDULE_MONITORING)) {
                    try {
                        rescheduleMonitoring();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_MONITORING_RESCHEDULE_ERROR);
                        BeaconTrackService.this.sendBroadcast(callbackIntent);
                    }

                } else if (intent.getAction().equals(ACTION_STOP_MONITORING)) {
                    try {
                        stopMonitoring();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_MONITORING_STOP_ERROR);
                        BeaconTrackService.this.sendBroadcast(callbackIntent);
                    }

                } else if (intent.getAction().equals(ACTION_RESCHEDULE_RANGING)) {
                    try {
                        rescheduleRanging();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_RANGING_RESCHEDULE_ERROR);
                        BeaconTrackService.this.sendBroadcast(callbackIntent);
                    }

                } else if (intent.getAction().equals(ACTION_STOP_RANGING)) {
                    try {
                        stopRanging();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_RANGING_STOP_ERROR);
                        BeaconTrackService.this.sendBroadcast(callbackIntent);
                    }
                } else if (intent.getAction().equals(ACTION_SET_FOREGROUND_SCAN_PERIOD)) {
                    long milis = intent.getExtras().getLong("milis");
                    changeForegroundScanPeriod(milis);
                } else if (intent.getAction().equals(ACTION_SET_BACKGROUND_SCAN_PERIOD)) {
                    long milis = intent.getExtras().getLong("milis");
                    changeBackgroundScanPeriod(milis);
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        unregisterTrackBeaconReceiver();
        if (iBeaconManager.isBound(this)) {
            iBeaconManager.unBind(this);
        }
        super.onDestroy();
    }

    private void unregisterTrackBeaconReceiver() {
        unregisterReceiver(trackServiceReceiver);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if (!BeaconTrackServiceSettings.shouldAlwaysWorkInBackground(this)) {
            if (iBeaconManager.isBound(this)) {
                iBeaconManager.unBind(this);
            }
            Intent intent = new Intent(this, BeaconTrackService.class);
            this.stopService(intent);
            return;
        }
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onIBeaconServiceConnect() {
        iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                ParcelableRegion parcelableRegion = new ParcelableRegion();
                parcelableRegion.setMajor(region.getMajor());
                parcelableRegion.setMinor(region.getMinor());
                parcelableRegion.setProximityUuid(region.getProximityUuid());
                parcelableRegion.setUniqueId(region.getUniqueId());

                Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_MONITORING_ENTER);
                callbackIntent.putExtra("region", parcelableRegion);
                BeaconTrackService.this.sendBroadcast(callbackIntent);
            }

            @Override
            public void didExitRegion(Region region) {
                ParcelableRegion parcelableRegion = new ParcelableRegion();
                parcelableRegion.setMajor(region.getMajor());
                parcelableRegion.setMinor(region.getMinor());
                parcelableRegion.setProximityUuid(region.getProximityUuid());
                parcelableRegion.setUniqueId(region.getUniqueId());

                Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_MONITORING_EXIT);
                callbackIntent.putExtra("region", parcelableRegion);
                BeaconTrackService.this.sendBroadcast(callbackIntent);
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                ParcelableRegion parcelableRegion = new ParcelableRegion();
                parcelableRegion.setMajor(region.getMajor());
                parcelableRegion.setMinor(region.getMinor());
                parcelableRegion.setProximityUuid(region.getProximityUuid());
                parcelableRegion.setUniqueId(region.getUniqueId());

                Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_MONITORING_DETERMINE_STATE);
                callbackIntent.putExtra("region", parcelableRegion);
                BeaconTrackService.this.sendBroadcast(callbackIntent);

            }
        });

        iBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
                ParcelableRegion parcelableRegion = new ParcelableRegion();
                parcelableRegion.setMajor(region.getMajor());
                parcelableRegion.setMinor(region.getMinor());
                parcelableRegion.setProximityUuid(region.getProximityUuid());
                parcelableRegion.setUniqueId(region.getUniqueId());

                List <ParcelableIBeacon> tempIBeaconList = new ArrayList<ParcelableIBeacon>();
                List <IBeacon> IBeaconList = new ArrayList(iBeacons);
                for (int i=0; i<iBeacons.size(); i++) {
                    ParcelableIBeacon tempIBeacon = new ParcelableIBeacon();
                    tempIBeacon.setProximityUuid(IBeaconList.get(i).getProximityUuid());
                    tempIBeacon.setProximity(IBeaconList.get(i).getProximity());
                    tempIBeacon.setAccuracy(IBeaconList.get(i).getAccuracy());
                    tempIBeacon.setBluetoothAddress(IBeaconList.get(i).getBluetoothAddress());
                    tempIBeacon.setMajor(IBeaconList.get(i).getMajor());
                    tempIBeacon.setMinor(IBeaconList.get(i).getMinor());
                    tempIBeacon.setRssi(IBeaconList.get(i).getRssi());
                    tempIBeacon.setTxPower(IBeaconList.get(i).getTxPower());

                    tempIBeaconList.add(tempIBeacon);
                }
                ParcelableIBeaconList parcelableIBeaconList = new ParcelableIBeaconList();
                parcelableIBeaconList.setParcelableIBeaconList(tempIBeaconList);

                Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_RANGING_DID_RANGE);
                callbackIntent.putExtra("region", parcelableRegion);
                callbackIntent.putExtra("beaconList", parcelableIBeaconList);
                BeaconTrackService.this.sendBroadcast(callbackIntent);
            }
        });

        Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_BEACON_SERVICE_CONNECTED);
        BeaconTrackService.this.sendBroadcast(callbackIntent);
    }

    private void addRegion(Region region) {
        boolean exists = false;
        for (int i=0; i<regionList.size(); i++) {
            if (region.equals(regionList.get(0))) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            regionList.add(region);
        }
    }

    private void removeRegion(Region region) {
        for (int i=0; i<regionList.size(); i++) {
            if (region.equals(regionList.get(0))) {
                regionList.remove(i);
            }
        }
    }

    private void rescheduleMonitoring() throws RemoteException {
        for (int i=0; i<regionList.size(); i++) {
            iBeaconManager.startMonitoringBeaconsInRegion(regionList.get(i));
        }
    }

    private void stopMonitoring() throws RemoteException {
        for (int i=0; i<iBeaconManager.getMonitoredRegions().size(); i++) {
            iBeaconManager.stopMonitoringBeaconsInRegion(new ArrayList<Region>(iBeaconManager.getMonitoredRegions()).get(i));
        }
    }

    private void rescheduleRanging() throws RemoteException {
        for (int i=0; i<regionList.size(); i++) {
            iBeaconManager.startRangingBeaconsInRegion(regionList.get(i));
        }
    }

    private void stopRanging() throws RemoteException {
        for (int i=0; i<iBeaconManager.getMonitoredRegions().size(); i++) {
            iBeaconManager.stopRangingBeaconsInRegion(new ArrayList<Region>(iBeaconManager.getRangedRegions()).get(i));
        }
    }

    private void changeForegroundScanPeriod(long milis) {
        iBeaconManager.setForegroundScanPeriod(milis);
        try {
            iBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void changeBackgroundScanPeriod(long milis) {
        iBeaconManager.setBackgroundScanPeriod(milis);
        try {
            iBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}