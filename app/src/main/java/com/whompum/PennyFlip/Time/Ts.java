package com.whompum.PennyFlip.Time;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import java.util.concurrent.TimeUnit;

/**
 * Wraps a Date time object to represent times
 * as well as providing very usefull utility functions like formatting and such
 */
public class Ts {

    public static final int AM = 0;
    public static final int PM = 1;

    public static final long DAY_MILLIS = TimeUnit.DAYS.toMillis(1);

    private DateTime date;

    private Ts(final long milliseconds){
        date = new DateTime(milliseconds);
    }

    public static Ts now(){
        return new Ts(System.currentTimeMillis());
    }

    public static Ts from(final long time){
        return new Ts(time);
    }

    public static Ts fromProjection(final int days){
        //creates a DateTime to represent a value X days ahead of right now
        return new Ts(System.currentTimeMillis() + (DAY_MILLIS*days));
    }


    //Returns OUR 12 AM
    public long getStartOfDay(){
        return (getMillis() - date.getMillisOfDay());
    }

    public long getMillis(){
        return date.getMillis();
    }

    //2018+
    public int getYear(){
        return date.getYear();
    }

    //Returns 1 - 12
    public int getMonth(){
        return date.getMonthOfYear();
    }

    public int getDay(){
        return date.getDayOfYear();
    }

    public int getMilitaryHour(){
        return date.getHourOfDay();
    }

    public int getStandardHour(){
        return date.get(DateTimeFieldType.hourOfHalfday());
    }

    public int getMinute(){
        return date.getMinuteOfDay();
    }

    public boolean isMorning(){
        return date.get(DateTimeFieldType.halfdayOfDay()) == AM;
    }



}
