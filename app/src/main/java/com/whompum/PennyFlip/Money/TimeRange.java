package com.whompum.PennyFlip.Money;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class TimeRange implements Parcelable {

    private long millisFloor, millisCiel;

    public static Creator<TimeRange> CREATOR = new Creator<TimeRange>() {
        @Override
        public TimeRange createFromParcel(Parcel source) {
            return new TimeRange( source );
        }

        @Override
        public TimeRange[] newArray(int size) {
            return new TimeRange[0];
        }
    };

    public TimeRange(final long m1, final long m2){
        if(m1 >= m2){
            millisFloor = m2;
            millisCiel = m1;
        }else{
            millisFloor = m1;
            millisCiel = m2;
        }
    }

    public TimeRange(@NonNull final Parcel p){
        this( p.readLong(), p.readLong() );
    }

    public TimeRange() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong( millisFloor );
        dest.writeLong( millisCiel );
    }

    public long getMillisFloor() {
        return millisFloor;
    }

    public long getMillisCiel() {
        return millisCiel;
    }

    public void setMillisFloor(long millisFloor) {
        this.millisFloor = millisFloor;
    }

    public void setMillisCiel(long millisCiel) {
        this.millisCiel = millisCiel;
    }
}
