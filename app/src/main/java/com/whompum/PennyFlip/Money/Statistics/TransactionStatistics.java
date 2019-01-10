package com.whompum.PennyFlip.Money.Statistics;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.IntRange;

import java.io.Serializable;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.ADD;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.SPEND;

@Entity
public class TransactionStatistics implements Serializable {

    public static final int ID_UNKWOWN = -1;

    @PrimaryKey
    @IntRange(from = ADD, to = SPEND)
    private int transactionType;

    //Total pennies under this Transaction Type
    private long netAmount;

    //Total # of transactions under this type
    private long numTransactions;

    //Id of the last transaction
    private int lastTransactionId;

    public TransactionStatistics(int transactionType, long netAmount, long numTransactions, int lastTransactionId) {
        this.transactionType = transactionType;
        this.netAmount = netAmount;
        this.numTransactions = numTransactions;
        this.lastTransactionId = lastTransactionId;
    }

    public TransactionStatistics(int transactionType, long netAmount, long numTransactions) {
        this( transactionType, netAmount, numTransactions, -1 );
    }

    public void incrementNumTransactions(){
        this.numTransactions++;
    }

    public void appendPennies(final long pennies){
        this.netAmount += pennies;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public long getNetAmount() {
        return netAmount;
    }

    public long getNumTransactions() {
        return numTransactions;
    }

    public int getLastTransactionId() {
        return lastTransactionId;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public void setNetAmount(long netAmount) {
        this.netAmount = netAmount;
    }

    public void setNumTransactions(long numTransactions) {
        this.numTransactions = numTransactions;
    }

    public void setLastTransactionId(int lastTransactionId) {
        this.lastTransactionId = lastTransactionId;
    }
}
