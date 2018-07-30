package com.whompum.PennyFlip.Transactions.Header;

import com.whompum.PennyFlip.Time.Ts;
import com.whompum.PennyFlip.Transactions.Header.HeaderItem;

/**
 * Created by bryan on 1/7/2018.
 */

public class TransactionHeaderItem implements HeaderItem {

    private Ts timestamp;
    private int numTransactions;

    public TransactionHeaderItem(Ts timestamp, final int numTransactions){
        this.timestamp = timestamp;
        this.numTransactions = numTransactions;
    }

    public void setNumTransactions(final int numTransactions){
        this.numTransactions = numTransactions;
    }

    public CharSequence getDate() {
        return Ts.simpleDate(timestamp);
    }

    public Ts getTimestamp(){
        return timestamp;
    }

    public String getNumTransactions(){
        return String.valueOf(numTransactions);
    }


}
