package com.whompum.PennyFlip.Transaction.Models;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

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


    public static final int ADD = 1;
    public static final int SPEND = 10;


    @TYPE private int transactionType = -1;

    @IntDef(ADD)
    public @interface TYPE{}




    public Transactions(@NonNull @TYPE final int type, final long pennyAmount, final long originalAmount, final String sourceName){
        this(type, Long.MIN_VALUE, pennyAmount, originalAmount, sourceName);
    }


    public Transactions(@NonNull @TYPE final int type, final long millis, final long pennyAmount, final long originalAmount, final String sourceName){
        this.pennyAmount = pennyAmount;
        this.sourceName = sourceName;
        this.originalAmount = originalAmount;

        if(millis == Long.MIN_VALUE)
            timestamp = Timestamp.now();
        else
            timestamp = Timestamp.from(millis);

        this.transactionType = type;

    }




    public Timestamp getTimestamp(){
        return timestamp;
    }

    public long getTransactionAmount(){
        return pennyAmount;
    }

    public long getOriginalTransactionAmount(){return originalAmount;}

    public String getSourceName(){
        return sourceName;
    }

    public int getTransactionType(){
        return transactionType;
    }

    public String simpleTime(){
        return PennyFlipTimeFormatter.simpleTime(timestamp);
    }

    public String simpleDate(){
        return PennyFlipTimeFormatter.simpleDate(timestamp);
    }


}












