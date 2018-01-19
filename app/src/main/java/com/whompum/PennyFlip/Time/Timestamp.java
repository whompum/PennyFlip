package com.whompum.PennyFlip.Time;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

/**
 * Simple container class for a timestamp.
 * Represent a current instance in time, and wraps a DateTime for simplicity.
 *
 * FIELDS
 *
 * YEAR
 * MONTH
 * WEEK?
 *
 * MONTHDAY
 * WEEKDAY
 *
 * HOUR
 * MINUTE
 * CYCLE
 *
 */

public class Timestamp {

    private DateTime timestamp;

    private Timestamp(){
        this.timestamp = DateTime.now();
    }

    private Timestamp(final long millis){
        this.timestamp = new DateTime(millis);
    }

    public static Timestamp now(){
        return new Timestamp();
    }

    public static Timestamp from(final long millis){
        return new Timestamp(millis);
    }


    public DateTime getTimestamp(){
        return timestamp;
    }


    public long millis(){
        return timestamp.getMillis();
    }

    public int year(){
        return timestamp.getYear();
    }

    public int month(){ return timestamp.getMonthOfYear();}

    public int day(){
        return timestamp.getDayOfMonth();
    }

    public int weekDay(){
        return timestamp.getDayOfWeek();
    }

    public int semiHourOfDay(){
        return timestamp.get(DateTimeFieldType.hourOfHalfday());
    }
    public int wholeHourOfDay(){return timestamp.get(DateTimeFieldType.hourOfDay()); }


    public int amPm(){
        return timestamp.get(DateTimeFieldType.halfdayOfDay());
    }

    public int minute(){return timestamp.getMinuteOfHour(); }

    public int second(){return timestamp.getSecondOfMinute(); }



}
