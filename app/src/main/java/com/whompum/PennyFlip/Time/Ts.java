package com.whompum.PennyFlip.Time;

import android.support.annotation.NonNull;

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

    public static final String MORNING = " AM";
    public static final String AFTERNOON = " PM";

    public static final long DAY_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static final String DATE_SEP = "/";

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
        return date.getMinuteOfHour();
    }

    public boolean isMorning(){
        return date.get(DateTimeFieldType.halfdayOfDay()) == AM;
    }

    public static String getPreferentialDate(@NonNull final Ts t){

        //Returns either simpleTime, or simpleDate depending on now.

        final long floor = Ts.now().getStartOfDay();
        final long ciel = floor + DAY_MILLIS;

        if(t.getMillis() >= floor && t.getMillis() < ciel)
            return simpleTime(t);

        return simpleDate(t);
    }

    public static String complexDate(@NonNull final Ts t){
        return simpleDate(t) + " " + simpleTime(t);
    }

    public static String simpleTime(@NonNull final Ts t){

        int h = t.getStandardHour();
        int m = t.getMinute();

        if(h == 0) h = 12;

        String hour;
        String min;

        hour = (h < 10) ? "0"+h : String.valueOf(h);
        min  =  (m < 10)  ? "0"+m  : String.valueOf(m);

        return hour + ":" + min + ((t.isMorning()) ? MORNING : AFTERNOON);
    }

    public static String simpleDate(@NonNull final Ts t){
        final int monthNum = t.getMonth();

        final String month = (monthNum < 10) ? "0"+String.valueOf(monthNum) : String.valueOf(monthNum);

        final int yearNum = t.getYear();

        final String y = String.valueOf(yearNum);

        final String year = y.substring(y.length()-2, y.length());

        return month+DATE_SEP+year;
    }

}
