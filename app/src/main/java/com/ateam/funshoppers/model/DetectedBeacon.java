

package com.ateam.funshoppers.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import com.ateam.funshoppers.util.BeaconUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;


public class DetectedBeacon extends Beacon implements IManagedBeacon {

    public static final int TYPE_EDDYSTONE_TLM = 32;
    public static final int TYPE_EDDYSTONE_UID = 0;
    public static final int TYPE_EDDYSTONE_URL = 16;
    public static final int TYPE_IBEACON_ALTBEACON = 1;

    protected long mLastSeen;

    public static final Parcelable.Creator<DetectedBeacon> CREATOR =
            new Parcelable.Creator<DetectedBeacon>() {
                @Override
                public DetectedBeacon createFromParcel(Parcel in) {
                    Beacon b = Beacon.CREATOR.createFromParcel(in);
                    DetectedBeacon dbeacon = new DetectedBeacon(b);
                    dbeacon.mLastSeen = in.readLong();
                    return dbeacon;
                }

                @Override
                public DetectedBeacon[] newArray(int size) {
                    return new DetectedBeacon[size];
                }
            };

    public DetectedBeacon(Beacon paramBeacon) {
        super(paramBeacon);
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeLong(mLastSeen);
    }


    @Override
    public long getTimeLastSeen() {
        return this.mLastSeen;
    }

    public void setTimeLastSeen(long lastSeen) {
        this.mLastSeen = lastSeen;
    }

    @Override
    public boolean equalTo(IManagedBeacon target) {
        return getId().equals(target);
    }

    @Override
    public boolean isEddyStoneTLM() {
        return getBeaconTypeCode() == TYPE_EDDYSTONE_TLM;
    }

    @Override
    public boolean isEddyStoneUID() {
        return getBeaconTypeCode() == TYPE_EDDYSTONE_UID;
    }

    @Override
    public boolean isEddyStoneURL() {
        return getBeaconTypeCode() == TYPE_EDDYSTONE_URL;
    }

    @Override
    public boolean isEddystone() {
        return (getBeaconTypeCode() == TYPE_EDDYSTONE_UID)
                || (getBeaconTypeCode() == TYPE_EDDYSTONE_URL) || (getBeaconTypeCode() == TYPE_EDDYSTONE_TLM);
    }

    @Override
    public BeaconType getBeaconType() {
        if (isEddystone()) {
            switch (getBeaconTypeCode()) {
                case TYPE_IBEACON_ALTBEACON:
                    return BeaconType.ALTBEACON;
                case TYPE_EDDYSTONE_TLM:
                    return BeaconType.EDDYSTONE_TLM;
                case TYPE_EDDYSTONE_UID:
                    return BeaconType.EDDYSTONE_UID;
                case TYPE_EDDYSTONE_URL:
                    return BeaconType.EDDYSTONE_URL;
                default:
                    return BeaconType.EDDYSTONE;
            }
        }
        return BeaconType.IBEACON;
    }

    public Identifier getId2() {
        if (isEddyStoneURL()) {
            return Identifier.parse("");
        }
        return super.getId2();
    }

    public Identifier getId3() {
        if (isEddystone()) {
            return Identifier.parse("");
        }
        return super.getId3();
    }

    @Override
    public String getId() {
        return getUUID() + ";" + getMajor() + ";" + getMinor() + ";" + getBluetoothAddress();
    }

    @Override
    public int getType() {
        return getBeaconTypeCode();
    }

    @Override
    public String getUUID() {
        return getId1().toString();
    }


    @Override
    public String getMajor() {
        if (isEddystone()) {
            return getId2().toHexString();
        }
        return getId2().toString();
    }

    @Override
    public String getMinor() {
        return getId3().toString();
    }

    @Override
    public String getEddystoneURL() {
        return UrlBeaconUrlCompressor.uncompress(getId1().toByteArray());
    }

    @Override
    public String toString() {
        if (isEddystone()) {
            if (isEddyStoneUID()) {
                return "Namespace: " + getUUID() + ", Instance: " + getMajor() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + BeaconUtil.getRoundedDistance(getDistance()) + "m";
            }
            if (isEddyStoneURL()) {
                Log.d("nicks",getEddystoneURL());
                return "URL: " + getEddystoneURL() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + BeaconUtil.getRoundedDistance(getDistance()) + "m";
            }
            return "UUID: " + getUUID() + ", Major: " + getMajor() + ", Minor: " + getMinor() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + BeaconUtil.getRoundedDistance(getDistance()) + "m";
        }
        return "UUID: " + getUUID() + ", Major: " + getMajor() + ", Minor: " + getMinor() + "\n" + "RSSI: " + getRssi() + " dBm, TX: " + getTxPower() + " dBm\n" + "Distance: " + BeaconUtil.getRoundedDistance(getDistance()) + "m";
    }
}
