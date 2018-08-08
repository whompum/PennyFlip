package com.whompum.PennyFlip.Time;

import android.content.Context;
import android.content.SharedPreferences;

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


}
