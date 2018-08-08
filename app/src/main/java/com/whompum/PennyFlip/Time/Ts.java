package com.whompum.PennyFlip.Time;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormat;

import java.util.concurrent.TimeUnit;

/**
 * Wraps a Date time object to represent times
 * as well as providing very usefull utility functions like formatting and such
 */
public class Ts {

    public static final int AM = 0;
    public static final int PM = 1;

    public static final String MORNING = " AM";
    public static final String AFTERNOON = " PM";

    public static final long DAY_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static final String DATE_SEP = "/";

    private DateTime date;

    private Ts(final long milliseconds){
        set(milliseconds);
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

    public void set(final long millis){
        date = new DateTime(millis);
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
        return date.getDayOfMonth();
    }

    public int getMilitaryHour(){
        return date.getHourOfDay();
    }

    public int getStandardHour(){
        return date.get(DateTimeFieldType.hourOfHalfday());
    }

    public int getMinute(){
        return date.getMinuteOfHour();
    }

    public DateTime getDate() {
        return date;
    }

    public boolean isMorning(){
        return date.get(DateTimeFieldType.halfdayOfDay()) == AM;
    }

    public String simpleTime(){
        return DateTimeFormat.shortTime().print(getMillis());
    }

    public String simpleDate(){
        return DateTimeFormat.mediumDate().print(getMillis());
    }

    public String getPreferentialDate(){

        //Returns either simpleTime, or simpleDate depending on now.

        final long floor = getStartOfDay();
        final long ciel = floor + DAY_MILLIS;

        if(getMillis() >= floor && getMillis() < ciel)
            return simpleTime();

        return simpleDate();
    }

}
