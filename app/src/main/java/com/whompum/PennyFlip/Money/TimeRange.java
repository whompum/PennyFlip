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

    public TimeRange() {
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
