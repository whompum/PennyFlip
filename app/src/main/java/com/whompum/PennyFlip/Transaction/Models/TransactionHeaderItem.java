package com.whompum.PennyFlip.Transaction.Models;

import com.whompum.PennyFlip.Time.PennyFlipTimeFormatter;
import com.whompum.PennyFlip.Time.Timestamp;

/**
 * Created by bryan on 1/7/2018.
 */

public class TransactionHeaderItem implements HeaderItem {

    private Timestamp timestamp;
    private int numTransactions;

    public TransactionHeaderItem(Timestamp timestamp, final int numTransactions){
        this.timestamp = timestamp;
        this.numTransactions = numTransactions;
    }

    public CharSequence getDate() {
        return PennyFlipTimeFormatter.simpleDate(timestamp);
    }

    public Timestamp getTimestamp(){
        return timestamp;
    }

    public String getNumTransactions(){
        return String.valueOf(numTransactions);
    }


}
