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
    private TYPE transactionType;
    private String sourceName; //Maybe a reference to a source too?


    public Transactions(final long pennyAmount, final TYPE transactionType, final String sourceName){
        this(Long.MIN_VALUE, pennyAmount, transactionType, sourceName);
    }


    public Transactions(final long millis, final long pennyAmount, final TYPE transactionType, final String sourceName){
        this.pennyAmount = pennyAmount;
        this.transactionType = transactionType;
        this.sourceName = sourceName;

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

    public TYPE getTransactionType(){
        return transactionType;
    }

    public String getName(){
        return sourceName;
    }

    public String simpleTime(){
        return PennyFlipTimeFormatter.simpleTime(timestamp.getTimestamp());
    }


    public enum TYPE{
        SPENT, ADDED
    }

}












