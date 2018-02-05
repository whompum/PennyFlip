package com.whompum.PennyFlip.Transaction.Models;

import com.whompum.PennyFlip.Time.PennyFlipTimeFormatter;
import com.whompum.PennyFlip.Time.Timestamp;



public class Transactions {

    private Timestamp timestamp;

    private long pennyAmount;

    private String sourceName; //Maybe a reference to a source too?

    private long sourceId;

    private int transactionType = Integer.MIN_VALUE;



    private Transactions(){
    }


    public static Transactions now(final int type, final long pennyAmount, final String sourceName, final long sourceId){
        return from(type, Long.MIN_VALUE, pennyAmount, sourceName, sourceId);
    }

    public static Transactions from(final int type, final long millis, final long pennyAmount, final String sourceName, final long sourceId){

        final Transactions transactions =  new Transactions();

        transactions.transactionType = type;

        if(millis != Long.MIN_VALUE)
            transactions.timestamp = Timestamp.from(millis);
        else
            transactions.timestamp = Timestamp.now();

        transactions.pennyAmount = pennyAmount;

        transactions.sourceName = sourceName;
        transactions.sourceId = sourceId;

    return transactions;
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












