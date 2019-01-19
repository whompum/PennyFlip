package com.whompum.PennyFlip.Time;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.whompum.PennyFlip.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.concurrent.TimeUnit;

/**
 * Wraps a Date time object to represent times
 * as well as providing very usefull utility functions like formatting and such
 */
public class Timestamp {

    public static final String FORMAT_MMMM_YYYY = "MMMM, YYYY";

    public static final int AM = 0;
    public static final int PM = 1;

    public static final String MORNING = " AM";
    public static final String AFTERNOON = " PM";

    public static final long DAY_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static final String DATE_SEP = "/";

    private DateTime date;

    private Timestamp(final long milliseconds){
        set(milliseconds);
    }

    public static Timestamp now(){
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp from(final long time){
        return new Timestamp(time);
    }

    public static Timestamp fromProjection(final int days){
        //creates a DateTime to represent a value X days ahead of right now
        return new Timestamp(System.currentTimeMillis() + (DAY_MILLIS*days));
    }

    public static Timestamp fromPastProjection(final int days){
        return new Timestamp(System.currentTimeMillis() - (DAY_MILLIS*days));
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

    public int getMonthDay(){
        return date.getDayOfMonth();
    }

    public int getYearDay(){return date.getDayOfYear();}

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

    public long getEndOfMonthTimestamp(){
        return new DateTime( (getMonth() != 12) ? getYear() : getYear()+1, (getMonth() != 12) ? getMonth()+1 : 1, 1, 0, 0,0,0 )
                .getMillis();
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

        final long currTime = System.currentTimeMillis();

        if( currTime >= floor && currTime < ciel ) //If within today
            return simpleTime();

        return simpleDate();
    }

    /**
     * Returns a String representing the current time. Such as Yesterday
     * @return
     */
    @StringRes
    public int getStringPreferentialDate(){
        final long cacheMillis = getMillis(); //Caching millis real quick

        final long tsDay = getStartOfDay(); //Pre-millis change

        set(System.currentTimeMillis()); // Set the Timestamp to today

        final long nowDay = getStartOfDay(); //Post-Milis change

        set(cacheMillis); //Reseting

        if(tsDay == nowDay)
            return R.string.string_today;

    return -1;
    }

    public String getFormattedDate(@NonNull final String pattern){
        return date.toString( DateTimeFormat.forPattern( pattern ) );
    }

    @CheckResult
    public Days getActiveDays(){
        return Days.daysBetween( getDate(), new DateTime( System.currentTimeMillis() ) );
    }

}
