package pl.surecase.co.ibeaconapp.ibeaconapp.example.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import pl.surecase.co.ibeaconapp.ibeaconapp.R;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.receiver.BluetoothStateReceiverSettings;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.service.BeaconTrackServiceSettings;

public class SettingsActivity extends Activity {

    private CheckBox cbBoot;
    private CheckBox cbBackground;
    private CheckBox cbBLEnotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        cbBoot = (CheckBox) findViewById(R.id.cbBoot);
        cbBackground = (CheckBox) findViewById(R.id.cbBackground);
        cbBLEnotify = (CheckBox) findViewById(R.id.cbBLEnotify);

        setupCheckBoxes();
    }

    private void setupCheckBoxes() {
        if (BeaconTrackServiceSettings.shouldStartOnDeviceBoot(this)) {
            cbBoot.setChecked(true);
        } else {
            cbBoot.setChecked(false);
        }

        if (BeaconTrackServiceSettings.shouldAlwaysWorkInBackground(this)) {
            cbBackground.setChecked(true);
        } else {
            cbBackground.setChecked(false);
        }

        if (BluetoothStateReceiverSettings.isNotificationDisplayEnabled(this)) {
            cbBLEnotify.setChecked(true);
        } else {
            cbBLEnotify.setChecked(false);
        }

        cbBoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    BeaconTrackServiceSettings.setStartOnDeviceBoot(SettingsActivity.this, true);
                } else {
                    BeaconTrackServiceSettings.setStartOnDeviceBoot(SettingsActivity.this, false);
                }
            }
        });

        cbBackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    BeaconTrackServiceSettings.setAlwaysWorkInBackground(SettingsActivity.this, true);
                } else {
                    BeaconTrackServiceSettings.setAlwaysWorkInBackground(SettingsActivity.this, false);
                }
            }
        });

        cbBLEnotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    BluetoothStateReceiverSettings.setShowNotificationAboutSateChange(SettingsActivity.this, true);
                } else {
                    BluetoothStateReceiverSettings.setShowNotificationAboutSateChange(SettingsActivity.this, false);
                }
            }
        });
    }

}

