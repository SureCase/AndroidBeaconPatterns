package pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.receiver;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import pl.surecase.co.ibeaconapp.ibeaconapp.R;
import pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.utils.NotificationTools;


public class BluetoothStateChangeReceiver extends BroadcastReceiver {

    public static final int BLUETOOTH_TURNED_OFF_NOTIFICATION= 159357456;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction()) && BluetoothStateReceiverSettings.isNotificationDisplayEnabled(context)) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new NotificationCompat.Builder(context);
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                NotificationTools.postOngoingNotification(context.getString(R.string.ble_turned_off_title), context.getString(R.string.ble_turned_off_message),
                        BLUETOOTH_TURNED_OFF_NOTIFICATION, context, notificationManager, builder);
            } else if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {
                notificationManager.cancel(BLUETOOTH_TURNED_OFF_NOTIFICATION);
            }
        }
    }
}
