package pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * Created by surecase on 16/07/14.
 */
public class BeaconUtils {

     public static final int REQUEST_ENABLE_BT = 605011;

     static public double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return accuracy;
        }
    }

    static public boolean checkBluetoothAvaliability() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                return true;
            } else {
                return false;
            }
        }
    }

    static public void turnOnBluetoothIfNotAvaliable(Activity activity) {
        if (!checkBluetoothAvaliability()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, BeaconUtils.REQUEST_ENABLE_BT);
        }
    }

}
