package com.whompum.PennyFlip.ActivityHistory;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Time.MidnightTimestamp;
import com.whompum.PennyFlip.Time.Timestamp;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

/**
 * Created by bryan on 1/31/2018.
 */

public class Timerange {

    private Timestamp floor;
    private Timestamp ciel;

    private Timerange(){

    }

    public static Timerange get(final long floorMillis, final long cielMillis){
        return get(Timestamp.from(floorMillis), Timestamp.from(cielMillis));
    }

    public static Timerange get(@NonNull final Timestamp floor,@NonNull final Timestamp ciel){
        final Timerange range =  new Timerange();
        range.floor = floor;
        range.ciel = ciel;

    return range;
    }

    public static Timerange get(@NonNull final DateTime floor, @NonNull final DateTime ciel){
        return get(floor.getMillis(), ciel.getMillis());
    }


    /**
     * Returns the first midnight (12:00AM) of the floors date
     * @return
     */
    public long getFloorTimerange(){
        return MidnightTimestamp.from(floor.millis()).getTodayMidnightMillis();
    }


    /**
     *
     * Add one day to the cieling timerange
     * because we need the midnight Timestamp of the next day since that is the
     * cap for the ciels time
     *
     * @return The first midnight (12:00AM) of the day that comes after the cieling.
     */
    public long getCielTimerange(){
        final long $oneDay = TimeUnit.DAYS.toMillis(1);
        return MidnightTimestamp.from(ciel.millis() + $oneDay).getTodayMidnightMillis();
    }





}










