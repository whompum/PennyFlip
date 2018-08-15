package com.whompum.PennyFlip.Time;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by bryan on 2/1/2018.
 */

public class UserStartDate {

    public static final String START_DATE_FILE = "startDate.txt";

    public static final String START_DATE_KEY = "startDate.ky";

    public static final long NO_TIME = Long.MIN_VALUE;

    public  long currTime;


    {
        currTime = System.currentTimeMillis();
    }

    private UserStartDate(final Context context){

        if( UserStartDate.getUserStartDate(context) != NO_TIME )  //The start time has been set. Do nothing
            return;

        getPreferences(context) //If we got this far, the start date hasn't been set. Let's continue, and set the start date.
                .edit()
                .putLong(START_DATE_KEY, currTime)
                .apply();

    }

    public static void set(final Context context){
        new UserStartDate(context);
    }

    public static long getUserStartDate(final Context context){
        return UserStartDate.getPreferences(context).getLong(START_DATE_KEY, NO_TIME);
    }

    private static SharedPreferences getPreferences(final  Context context){
       return context.getSharedPreferences(START_DATE_FILE, Context.MODE_PRIVATE);
    }

    public static List<String> getActiveYears(@NonNull final Context ctx){
        final List<String> years = new ArrayList<>();

        final int now = Timestamp.now().getYear();

        years.add(String.valueOf(now));

        final int startDateYear = Timestamp.from(
                UserStartDate.getUserStartDate(ctx)
        ).getYear();


        if(startDateYear != now){

            for(int year = startDateYear; year < now; year++)
                years.add(String.valueOf(year));
        }

        Collections.sort(years, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.valueOf(o1) - Integer.valueOf(o2);
            }
        });

        return years; //Return all available years the user has been active
    }

}
