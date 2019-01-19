package com.whompum.PennyFlip.Time;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Time.Timestamp;

import org.joda.time.MutableDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MonthlyTimestampResolver {

    public static List<Long> fetchActiveUserMonths(final long startDate){

        final List<Long> months = new ArrayList<>();

        final long currentMonthMillis = getMonthTimestamp( Timestamp.now() ).getMillis();

        //Start at the end month ( user start date )
        final MutableDateTime month = getMonthTimestamp( Timestamp.from( startDate ) );

        long currMillis = month.getMillis();

        while( currMillis != currentMonthMillis ){

            months.add( currMillis );

            month.addMonths( 1 );

            currMillis = month.getMillis();

        }

        months.add( currentMonthMillis );

        Collections.sort( months,  (o1, o2) -> (int)(o1-o2) );

        return months;
    }

    public static MutableDateTime getMonthTimestamp(@NonNull final Timestamp timestamp){
        return new MutableDateTime( timestamp.getYear(), timestamp.getMonth(), 1, 0, 0, 0, 0);
    }

}
