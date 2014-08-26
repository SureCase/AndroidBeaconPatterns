package pl.surecase.co.ibeaconapp.ibeaconapp.example.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.radiusnetworks.ibeacon.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.surecase.co.ibeaconapp.ibeaconapp.R;
import pl.surecase.co.ibeaconapp.ibeaconapp.example.utils.BeaconDataStore;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.base.BeaconTrackActivity;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.service.BeaconTrackServiceHelper;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.utils.BeaconUtils;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableIBeacon;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableIBeaconList;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model.ParcelableRegion;

public class KalmanPatternActivity extends BeaconTrackActivity {

    private List<Double> distanceBuffor = new ArrayList<Double>();
    private List<Double> medianBuffor = new ArrayList<Double>();

    private TextView tvMajor;
    private TextView tvMinor;
    private TextView tvDistance;
    private TextView tvState;
    private EditText etHigherThreshold;
    private EditText etLowerThreshold;

    private double lastError = 1;
    private double kalmanEstimation = 0;
    private double lowerThreshold = 0.4;
    private double higherThreshold = 0.9;
    private int bufforSize = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalman_pattern);

        tvMajor = (TextView) findViewById(R.id.tvMajor);
        tvMinor = (TextView) findViewById(R.id.tvMinor);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvState = (TextView) findViewById(R.id.tvState);
        etHigherThreshold = (EditText) findViewById(R.id.etHigherThreshold);
        etLowerThreshold = (EditText) findViewById(R.id.etLowerThreshold);

        etHigherThreshold.setText(Double.toString(higherThreshold));
        etLowerThreshold.setText(Double.toString(lowerThreshold));

        if (!BeaconUtils.checkBluetoothAvaliability()) {
            BeaconUtils.turnOnBluetoothIfNotAvaliable(this);
        }

        BeaconTrackServiceHelper.restartBeaconTrackSerivce(getApplication());

        setupViewWithSaveBeaconData();
        setupEditText();
    }

    private void setupViewWithSaveBeaconData() {
        if (BeaconDataStore.getBeaconMajor(this) != 0 && BeaconDataStore.getBeaconMinor(this) != 0) {
            tvMajor.setText("Major: " + BeaconDataStore.getBeaconMajor(this));
            tvMinor.setText("Minor: " + BeaconDataStore.getBeaconMinor(this));
        }
    }

    private void setupEditText() {
        etHigherThreshold.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null && !s.toString().equals("")) {
                    higherThreshold = Double.parseDouble(s.toString());
                }
            }
        });

        etLowerThreshold.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null && !s.toString().equals("")) {
                    lowerThreshold = Double.parseDouble(s.toString());
                }
            }
        });
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

            double varianceSum = 0;
            double expectedDistance = 0;
            double tempDistance = BeaconUtils.calculateAccuracy(list.get(0).getTxPower(), list.get(0).getRssi());

            if (distanceBuffor.size() == bufforSize) {
                distanceBuffor.remove(0);
            }

            distanceBuffor.add((tempDistance));
            if (list.get(0).getProximity() == ParcelableIBeacon.PROXIMITY_IMMEDIATE) {
                expectedDistance = 0.8625;
                lastError = 1;
                kalmanEstimation = 0;
            } else if (list.get(0).getProximity() == ParcelableIBeacon.PROXIMITY_NEAR) {
                expectedDistance = 1.75;
                lastError = 1;
                kalmanEstimation = 0;
            } else if (list.get(0).getProximity() == ParcelableIBeacon.PROXIMITY_FAR) {
                expectedDistance = 2.625;
                lastError = 1;
                kalmanEstimation = 0;
            }

            for (int k = 0; k < distanceBuffor.size(); k++) {
                varianceSum = varianceSum + (distanceBuffor.get(k) - expectedDistance) * (distanceBuffor.get(k) - expectedDistance);
            }
            double variance = varianceSum / bufforSize;
            double standardDeviation = Math.sqrt(variance);

            double kalmanParameter = lastError / (lastError + standardDeviation);
            kalmanEstimation = kalmanEstimation + kalmanParameter * (tempDistance - kalmanEstimation);
            lastError = (1 - kalmanParameter) * lastError;

            if (medianBuffor.size() >= bufforSize) {
                medianBuffor.remove(0);

            }
            medianBuffor.add(kalmanEstimation);
            List<Double> medianBufforSorted = new ArrayList<Double>();
            medianBufforSorted.clear();
            medianBufforSorted.addAll(medianBuffor);
            Collections.sort(medianBufforSorted);
            double median = median(medianBufforSorted);

            if (median < lowerThreshold) {
                //instruction for close distance
                tvState.setText(getString(R.string.state_close));
                tvState.setTextColor(getResources().getColor(R.color.green));

            } else if (median > higherThreshold) {
                //operation for far distance
                tvState.setText(getString(R.string.state_far));
                tvState.setTextColor(getResources().getColor(R.color.red));
            }

            tvDistance.setText(String.format( "%.5f", median));
        }
    }

    private double median(List<Double> a){
        int middle = a.size()/2;
        if (a.size() % 2 == 1) {
            return a.get(middle);
        } else {
            return (a.get(middle-1) + a.get(middle)) / 2.0;
        }
    }

    @Override
    protected void onDestroy() {
        BeaconTrackServiceHelper.stopBeaconTrackSerivce(getApplication());
        super.onDestroy();
    }
}

