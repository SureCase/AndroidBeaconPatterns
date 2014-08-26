package pl.surecase.co.ibeaconapp.ibeaconapp.example.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.radiusnetworks.ibeacon.Region;

import java.util.ArrayList;
import java.util.List;

import pl.surecase.co.ibeaconapp.ibeaconapp.R;
import pl.surecase.co.ibeaconapp.ibeaconapp.example.utils.BeaconDataStore;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.base.BeaconTrackActivity;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.service.BeaconTrackServiceHelper;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.utils.BeaconUtils;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableIBeacon;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableIBeaconList;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableRegion;

public class SetupBeaconActivity extends BeaconTrackActivity {

    private TextView tvHint;
    private TextView tvStatus;
    private ImageButton ibBeacon;

    private boolean isOnlyOneBeaconAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_beacon);

        tvHint = (TextView) findViewById(R.id.tvHint);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        ibBeacon = (ImageButton) findViewById(R.id.ibBeacon);

        if (!BeaconUtils.checkBluetoothAvaliability()) {
            BeaconUtils.turnOnBluetoothIfNotAvaliable(this);
        }
        BeaconTrackServiceHelper.restartBeaconTrackSerivce(getApplication());
    }

    @Override
    public void onBeaconServiceConnected() {
        super.onBeaconServiceConnected();
        BeaconTrackServiceHelper.setBackgroundScanPeriod(this, 300);
        BeaconTrackServiceHelper.setForegroundScanPeriod(this, 300);
        BeaconTrackServiceHelper.addRegion(this, new Region("global", BeaconDataStore.ESTIMOTE_UUID, null , null));
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
            final List<ParcelableIBeacon> list = iBeaconList.getParcelableIBeaconList();
            final List<ParcelableIBeacon> beaconsInRangeList = new ArrayList<ParcelableIBeacon>();

            double rangeDistance = 0.50;

            for (int i=0; i<list.size(); i++) {
                if (BeaconUtils.calculateAccuracy(list.get(i).getTxPower(), list.get(i).getRssi()) < rangeDistance) {
                    beaconsInRangeList.add(list.get(i));
                }
            }

            if (beaconsInRangeList.size() == 1) {
                isOnlyOneBeaconAvailable = true;
                tvHint.setVisibility(View.VISIBLE);
                tvStatus.setText(getString(R.string.setup_status_found));

                ibBeacon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isOnlyOneBeaconAvailable) {
                            BeaconDataStore.setBeaconMajor(SetupBeaconActivity.this, beaconsInRangeList.get(0).getMajor());
                            BeaconDataStore.setBeaconMinor(SetupBeaconActivity.this, beaconsInRangeList.get(0).getMinor());
                            Toast.makeText(getApplicationContext(), getString(R.string.setup_confirm), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });

            } else if (beaconsInRangeList.size() == 0) {
                isOnlyOneBeaconAvailable = false;
                tvHint.setVisibility(View.INVISIBLE);
                tvStatus.setText(getString(R.string.setup_status_not_found));
                ibBeacon.setOnClickListener(null);

            } else if (beaconsInRangeList.size() > 1) {
                isOnlyOneBeaconAvailable = false;
                tvHint.setVisibility(View.INVISIBLE);
                tvStatus.setText(getString(R.string.setup_status_too_much));
                ibBeacon.setOnClickListener(null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        BeaconTrackServiceHelper.stopBeaconTrackSerivce(getApplication());
        super.onDestroy();
    }
}
