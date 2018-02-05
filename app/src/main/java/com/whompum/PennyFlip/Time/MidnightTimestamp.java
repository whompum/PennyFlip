package com.whompum.PennyFlip.Time;

import org.joda.time.DateTime;

/**
 * Created by bryan on 1/18/2018.
 */

public class MidnightTimestamp {

    private long midnightMillis;


    private MidnightTimestamp(final long millis){
        this(Timestamp.from(millis));
    }

    private MidnightTimestamp(final Timestamp today){
        this.midnightMillis = new DateTime(today.year(), today.month(), today.day(), 0, 0).getMillis();
    }


    public static MidnightTimestamp today(){
        return new MidnightTimestamp(Timestamp.now());
    }

    public static MidnightTimestamp from(final long midnightMillis){
        return new MidnightTimestamp(midnightMillis);
    }

    public long getTodayMidnightMillis(){
        return midnightMillis;
    }


}
