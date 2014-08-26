package pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surecase on 15/07/14.
 */
public class ParcelableIBeaconList implements Parcelable {

    protected List<ParcelableIBeacon> parcelableIBeaconList;

    public List<ParcelableIBeacon> getParcelableIBeaconList() {
        return parcelableIBeaconList;
    }

    public ParcelableIBeaconList() {
    }

    public void setParcelableIBeaconList(List<ParcelableIBeacon> parcelableIBeaconList) {
        this.parcelableIBeaconList = parcelableIBeaconList;
    }

    protected ParcelableIBeaconList(Parcel in) {
        if (in.readByte() == 0x01) {
            parcelableIBeaconList = new ArrayList<ParcelableIBeacon>();
            in.readList(parcelableIBeaconList, ParcelableIBeacon.class.getClassLoader());
        } else {
            parcelableIBeaconList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (parcelableIBeaconList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(parcelableIBeaconList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ParcelableIBeaconList> CREATOR = new Parcelable.Creator<ParcelableIBeaconList>() {
        @Override
        public ParcelableIBeaconList createFromParcel(Parcel in) {
            return new ParcelableIBeaconList(in);
        }

        @Override
        public ParcelableIBeaconList[] newArray(int size) {
            return new ParcelableIBeaconList[size];
        }
    };

    @Override
    public String toString() {
        return "ParcelableIBeaconList{" +
                "parcelableIBeaconList=" + parcelableIBeaconList +
                '}';
    }
}
