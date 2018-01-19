package com.whompum.PennyFlip.Transaction.Models;

import com.whompum.PennyFlip.Time.PennyFlipTimeFormatter;
import com.whompum.PennyFlip.Time.Timestamp;

/**
 * Created by bryan on 12/23/2017.
 */

public class Transactions {

    public static final String TAG = "TIME";

    private Timestamp timestamp;

    private long pennyAmount;

    private String sourceName; //Maybe a reference to a source too?

    private int transactionType = Integer.MIN_VALUE;



    public Transactions(final int type, final long pennyAmount, final String sourceName){
        this(type, Long.MIN_VALUE, pennyAmount, sourceName);
    }


    public Transactions(final int type, final long millis, final long pennyAmount, final String sourceName){
        this.pennyAmount = pennyAmount;
        this.sourceName = sourceName;

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












