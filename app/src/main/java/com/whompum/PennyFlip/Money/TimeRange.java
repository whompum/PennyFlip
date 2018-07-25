package com.whompum.PennyFlip.Money;

public class TimeRange {

    private long millisFloor, millisCiel;

    public TimeRange(final long m1, final long m2){
        if(m1 >= m2){
            millisFloor = m2;
            millisCiel = m1;
        }else{
            millisFloor = m1;
            millisCiel = m2;
        }
    }

    public long getMillisFloor() {
        return millisFloor;
    }

    public long getMillisCiel() {
        return millisCiel;
    }

}
