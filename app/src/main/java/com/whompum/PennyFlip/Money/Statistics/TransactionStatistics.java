package com.whompum.PennyFlip.Money.Statistics;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.io.Serializable;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.INCOME;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.EXPENSE;

@Entity
public class TransactionStatistics implements Serializable {

    @PrimaryKey
    @IntRange(from = INCOME, to = EXPENSE)
    private int transactionType;

    //Id of the last transaction
    @Embedded
    @Nullable
    private TransactionMetData transaction;

    //Total pennies under this Transaction Type
    private long netAmount;

    //Total # of transactions under this type
    private long numTransactions;

    //The average of all the transactions under this transactionType
    private long transactionAverage;

    //Ratio of one transaction type to the next
    private double transactionRatio = 0D;

    public TransactionStatistics(int transactionType,
                                 long netAmount,
                                 long numTransactions,
                                 long transactionAverage,
                                 double transactionRatio,
                                 @Nullable TransactionMetData transaction) {
        this.transactionType = transactionType;
        this.transaction = transaction;
        this.netAmount = netAmount;
        this.numTransactions = numTransactions;
        this.transactionAverage = transactionAverage;
        this.transactionRatio = transactionRatio;
    }

    @Ignore
    public TransactionStatistics(final int transactionType){
        this.transactionType = transactionType;
    }

    public void updateFromTransaction(@NonNull final Transaction transaction){
        this.netAmount += transaction.getAmount();
        this.numTransactions++;
        this.transaction = new TransactionMetData( transaction );

        transactionAverage = netAmount / numTransactions;
    }

    public int getTransactionType() {
        return transactionType;
    }

    @Nullable
    public TransactionMetData getTransaction() {
        return transaction;
    }

    public long getNetAmount() {
        return netAmount;
    }

    public long getNumTransactions() {
        return numTransactions;
    }

    public long getTransactionAverage() {
        return transactionAverage;
    }

    public double getTransactionRatio() {
        return transactionRatio;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransaction(@Nullable TransactionMetData transaction) {
        this.transaction = transaction;
    }

    public void setNetAmount(long netAmount) {
        this.netAmount = netAmount;
    }

    public void setNumTransactions(long numTransactions) {
        this.numTransactions = numTransactions;
    }

    public void setTransactionAverage(long transactionAverage) {
        this.transactionAverage = transactionAverage;
    }

    public void setTransactionRatio(final double ratio){
        this.transactionRatio = ratio;
    }

    public static class TransactionMetData{
        private String title;
        private String sourceTitle;
        private long amount;

        @Ignore
        public TransactionMetData(@NonNull final Transaction transaction){
            this( transaction.getTitle(), transaction.getSourceId(), transaction.getAmount() );
        }

        public TransactionMetData(@Nullable final String title, @NonNull final String sourceTitle, final long amount){
            this.title = (title == null ) ? "NO_TITLE" : title;
            this.sourceTitle = sourceTitle;
            this.amount = amount;
        }

        public String getTitle() {
            return title;
        }

        public String getSourceTitle() {
            return sourceTitle;
        }

        public long getAmount() {
            return amount;
        }
    }

}
