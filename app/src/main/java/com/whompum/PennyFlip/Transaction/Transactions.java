package com.whompum.PennyFlip.Transaction;

import com.whompum.PennyFlip.Time.PennyFlipTimeFormatter;
import com.whompum.PennyFlip.Time.Timestamp;

/**
 * Created by bryan on 12/23/2017.
 */

public class Transactions {

    public static final String TAG = "TIME";

    private Timestamp timestamp;

    private long pennyAmount;

    private long originalAmount;

    private String sourceName; //Maybe a reference to a source too?


    public Transactions(final long pennyAmount, final long originalAmount, final String sourceName){
        this(Long.MIN_VALUE, pennyAmount, originalAmount, sourceName);
    }


    public Transactions(final long millis, final long pennyAmount, final long originalAmount, final String sourceName){
        this.pennyAmount = pennyAmount;
        this.sourceName = sourceName;
        this.originalAmount = originalAmount;

        if(millis == Long.MIN_VALUE)
            timestamp = Timestamp.now();
        else
            timestamp = Timestamp.from(millis);
    }

    public Timestamp getTimestamp(){
        return timestamp;
    }

    public long getAmount(){
        return pennyAmount;
    }

    public long getOriginalAmount(){return originalAmount;}

    public String getName(){
        return sourceName;
    }



    public String simpleTime(){
        return PennyFlipTimeFormatter.simpleTime(timestamp);
    }

    public String simpleDate(){
        return PennyFlipTimeFormatter.simpleDate(timestamp);
    }


}












