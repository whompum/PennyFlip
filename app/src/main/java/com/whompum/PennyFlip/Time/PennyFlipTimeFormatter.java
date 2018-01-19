package com.whompum.PennyFlip.Time;

import android.support.annotation.NonNull;

/**
 * Simply converts timestamps into various formatted strings.
 * E.G. if i would want the short time of a transaction
 * this class would compute HH:MM AM_PM for me
 */

public class PennyFlipTimeFormatter {

    public static final String HOUR_SEP = ":";

    public static final int AM = 0;
    public static final int PM = 1;

    private static StringBuilder formatter = new StringBuilder();



    /**
     * @return a formatted simple time such as 12:18 PM
     */
    public static String simpleTime(@NonNull final Timestamp timeStamp){

        formatter.delete(0, formatter.length());

        int hour = timeStamp.semiHourOfDay();

        if(hour == 0)
            formatter.append(12);
        else
            formatter.append(hour);

        formatter.append(HOUR_SEP);

        int minute = timeStamp.minute();

        if(minute < 10)
            formatter.append(0);

        formatter.append(minute);

        final int AM_PM = timeStamp.amPm();

        final String cycle = (AM_PM == AM) ? " AM" : " PM";

        formatter.append(cycle);

    return formatter.toString();
    }


    public static String simpleDate(@NonNull final Timestamp timeStamp){

        final int month = timeStamp.month();
        final int day = timeStamp.day();

        formatter.delete(0, formatter.length());


        if(month < 10)
            formatter.append(0);

        formatter.append(month);

        formatter.append("/");

        if(day < 10)
            formatter.append(0);

        formatter.append(day);

        return formatter.toString();
    }



}
