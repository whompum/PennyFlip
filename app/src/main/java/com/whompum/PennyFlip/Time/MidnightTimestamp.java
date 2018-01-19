package com.whompum.PennyFlip.Time;

import org.joda.time.DateTime;

/**
 * Created by bryan on 1/18/2018.
 */

public class MidnightTimestamp {

    private long midnightMillis;

    public MidnightTimestamp(){
        final Timestamp today = Timestamp.now();
        this.midnightMillis = new DateTime(today.year(), today.month(), today.day(), 0, 0).getMillis();
    }

    public long getTodayMidnightMillis(){
        return midnightMillis;
    }


}
