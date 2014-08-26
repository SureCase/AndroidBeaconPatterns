package pl.surecase.co.ibeaconapp.ibeaconapp.example.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.radiusnetworks.ibeacon.Region;

import java.util.List;

import pl.surecase.co.ibeaconapp.ibeaconapp.R;
import pl.surecase.co.ibeaconapp.ibeaconapp.example.utils.BeaconDataStore;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.base.BeaconTrackActivity;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.service.BeaconTrackServiceHelper;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.utils.BeaconUtils;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableIBeacon;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableIBeaconList;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableRegion;

public class ClassicPatternActivity extends BeaconTrackActivity {

    private TextView tvMajor;
    private TextView tvMinor;
    private TextView tvDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic);

        tvMajor = (TextView) findViewById(R.id.tvMajor);
        tvMinor = (TextView) findViewById(R.id.tvMinor);
        tvDistance = (TextView) findViewById(R.id.tvDistance);

        if (!BeaconUtils.checkBluetoothAvaliability()) {
            BeaconUtils.turnOnBluetoothIfNotAvaliable(this);
        }
        BeaconTrackServiceHelper.restartBeaconTrackSerivce(getApplication());

        setupViewWithSaveBeaconData();
    }

    private void setupViewWithSaveBeaconData() {
        if (BeaconDataStore.getBeaconMajor(this) != 0 && BeaconDataStore.getBeaconMinor(this) != 0) {
            tvMajor.setText("Major: " + BeaconDataStore.getBeaconMajor(this));
            tvMinor.setText("Minor: " + BeaconDataStore.getBeaconMinor(this));
        }
    }

    @Override
    public void onBeaconServiceConnected() {
        super.onBeaconServiceConnected();
        BeaconTrackServiceHelper.setBackgroundScanPeriod(this, 300);
        BeaconTrackServiceHelper.setForegroundScanPeriod(this, 300);
        BeaconTrackServiceHelper.addRegion(this, new Region("registered", BeaconDataStore.ESTIMOTE_UUID,
                BeaconDataStore.getBeaconMajor(this), BeaconDataStore.getBeaconMinor(this)));
        BeaconTrackServiceHelper.rescheduleMonitoring(this);
    }

    @Override
    public void onBeaconMonitoringEnter(ParcelableRegion region) {
        super.onBeaconMonitoringEnter(region);
        BeaconTrackServiceHelper.rescheduleRanging(this);
    }

    @Override
    public void onBeaconRanging(ParcelableIBeaconList iBeaconList, ParcelableRegion region) {
        super.onBeaconRanging(iBeaconList, region);
        if (iBeaconList != null && iBeaconList.getParcelableIBeaconList() != null && iBeaconList.getParcelableIBeaconList().size()>0) {
            List<ParcelableIBeacon> list = iBeaconList.getParcelableIBeaconList();
            tvDistance.setText(String.format( "%.5f", BeaconUtils.calculateAccuracy(list.get(0).getTxPower(), list.get(0).getRssi())) + "m");
        } else {
            tvDistance.setText("---");
        }
    }

    @Override
    protected void onDestroy() {
        BeaconTrackServiceHelper.stopBeaconTrackSerivce(getApplication());
        super.onDestroy();
    }
}

