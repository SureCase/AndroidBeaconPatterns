package pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by surecase on 15/07/14.
 */
public class ParcelableRegion implements Parcelable {

    protected Integer major;
    protected Integer minor;
    protected String proximityUuid;
    protected String uniqueId;

    public ParcelableRegion() {
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public String getProximityUuid() {
        return proximityUuid;
    }

    public void setProximityUuid(String proximityUuid) {
        this.proximityUuid = proximityUuid;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public ParcelableRegion(Parcel in) {
        major = in.readByte() == 0x00 ? null : in.readInt();
        minor = in.readByte() == 0x00 ? null : in.readInt();
        proximityUuid = in.readString();
        uniqueId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (major == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(major);
        }
        if (minor == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(minor);
        }
        dest.writeString(proximityUuid);
        dest.writeString(uniqueId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ParcelableRegion> CREATOR = new Parcelable.Creator<ParcelableRegion>() {
        @Override
        public ParcelableRegion createFromParcel(Parcel in) {
            return new ParcelableRegion(in);
        }

        @Override
        public ParcelableRegion[] newArray(int size) {
            return new ParcelableRegion[size];
        }
    };

    @Override
    public String toString() {
        return "ParcelableRegion{" +
                "major=" + major +
                ", minor=" + minor +
                ", proximityUuid='" + proximityUuid + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                '}';
    }
}