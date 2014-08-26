package pl.surecase.co.ibeaconapp.ibeaconapp.example.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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

public class SmoothingPatternActivity extends BeaconTrackActivity {

    private TextView tvMajor;
    private TextView tvMinor;
    private TextView tvDistance;
    private TextView tvState;
    private EditText etHigherThreshold;
    private EditText etLowerThreshold;
    private EditText etSmoothing;

    private int smoothing = 18;
    private double lowerThreshold = 1.3;
    private double higherThreshold = 2.0;
    private double filteredValue = (lowerThreshold + higherThreshold)/2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoothing_pattern);

        tvMajor = (TextView) findViewById(R.id.tvMajor);
        tvMinor = (TextView) findViewById(R.id.tvMinor);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvState = (TextView) findViewById(R.id.tvState);
        etHigherThreshold = (EditText) findViewById(R.id.etHigherThreshold);
        etLowerThreshold = (EditText) findViewById(R.id.etLowerThreshold);
        etSmoothing = (EditText) findViewById(R.id.etSmoothing);

        etHigherThreshold.setText(Double.toString(higherThreshold));
        etLowerThreshold.setText(Double.toString(lowerThreshold));
        etSmoothing.setText(Integer.toString(smoothing));

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

        etSmoothing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null && !s.toString().equals("")) {
                    smoothing = Integer.parseInt(s.toString());
                }
            }
        });
    }

    @Override
    public void onBeaconServiceConnected() {
        super.onBeaconServiceConnected();
        BeaconTrackServiceHelper.setBackgroundScanPeriod(this, 150);
        BeaconTrackServiceHelper.setForegroundScanPeriod(this, 150);
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
            double tempDistance = BeaconUtils.calculateAccuracy(list.get(0).getTxPower(), list.get(0).getRssi());

            filteredValue = filteredValue + ((tempDistance - filteredValue)/smoothing);


            if (filteredValue < lowerThreshold) {
                //instruction for close distance
                tvState.setText(getString(R.string.state_close));
                tvState.setTextColor(getResources().getColor(R.color.green));

            } else if (filteredValue > higherThreshold) {
                //operation for far distance
                tvState.setText(getString(R.string.state_far));
                tvState.setTextColor(getResources().getColor(R.color.red));
            }

            tvDistance.setText(String.format( "%.5f", filteredValue));
        }
    }

    @Override
    protected void onDestroy() {
        BeaconTrackServiceHelper.stopBeaconTrackSerivce(getApplication());
        super.onDestroy();
    }
}
