package pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by surecase on 15/07/14.
 */
public class ParcelableIBeacon implements Parcelable {

    public static final int PROXIMITY_IMMEDIATE = 1;
    public static final int PROXIMITY_NEAR = 2;
    public static final int PROXIMITY_FAR = 3;
    public static final int PROXIMITY_UNKNOWN = 0;

    protected String proximityUuid;
    protected int major;
    protected int minor;
    protected Integer proximity;
    protected Double accuracy;
    protected int rssi;
    protected int txPower;
    protected String bluetoothAddress;
    protected Double runningAverageRssi;

    public ParcelableIBeacon() {
    }

    public String getProximityUuid() {
        return proximityUuid;
    }

    public void setProximityUuid(String proximityUuid) {
        this.proximityUuid = proximityUuid;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public Integer getProximity() {
        return proximity;
    }

    public void setProximity(Integer proximity) {
        this.proximity = proximity;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getTxPower() {
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public Double getRunningAverageRssi() {
        return runningAverageRssi;
    }

    public void setRunningAverageRssi(Double runningAverageRssi) {
        this.runningAverageRssi = runningAverageRssi;
    }

    protected ParcelableIBeacon(Parcel in) {
        proximityUuid = in.readString();
        major = in.readInt();
        minor = in.readInt();
        proximity = in.readByte() == 0x00 ? null : in.readInt();
        accuracy = in.readByte() == 0x00 ? null : in.readDouble();
        rssi = in.readInt();
        txPower = in.readInt();
        bluetoothAddress = in.readString();
        runningAverageRssi = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(proximityUuid);
        dest.writeInt(major);
        dest.writeInt(minor);
        if (proximity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(proximity);
        }
        if (accuracy == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(accuracy);
        }
        dest.writeInt(rssi);
        dest.writeInt(txPower);
        dest.writeString(bluetoothAddress);
        if (runningAverageRssi == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(runningAverageRssi);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ParcelableIBeacon> CREATOR = new Parcelable.Creator<ParcelableIBeacon>() {
        @Override
        public ParcelableIBeacon createFromParcel(Parcel in) {
            return new ParcelableIBeacon(in);
        }

        @Override
        public ParcelableIBeacon[] newArray(int size) {
            return new ParcelableIBeacon[size];
        }
    };

    @Override
    public String toString() {
        return "ParcelableIBeacon{" +
                "proximityUuid='" + proximityUuid + '\'' +
                ", major=" + major +
                ", minor=" + minor +
                ", proximity=" + proximity +
                ", accuracy=" + accuracy +
                ", rssi=" + rssi +
                ", txPower=" + txPower +
                ", bluetoothAddress='" + bluetoothAddress + '\'' +
                ", runningAverageRssi=" + runningAverageRssi +
                '}';
    }
}