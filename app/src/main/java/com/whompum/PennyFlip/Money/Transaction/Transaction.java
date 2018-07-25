package com.whompum.PennyFlip.Money.Transaction;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Transactions.Models.TransactionType;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Source.class,
                                  parentColumns = "title",
                                  childColumns = "sourceId",
                                  onDelete = CASCADE,
                                  deferred = true))
public class Transaction {

    @PrimaryKey
    private int id;

    @Nullable
    private String title;

    private long timestamp;

    private long amount;

    @IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE)
    private int transactionType;

    public Transaction(int id, String title, long timestamp, long amount, int transactionType, @NonNull String sourceId) {
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
        this.amount = amount;
        this.transactionType = transactionType;
        this.sourceId = sourceId;
    }

    @Ignore
    public Transaction(@NonNull final String sourceId, final int transactionType, final long amount){
        this(sourceId, transactionType, null, amount);
    }


    @Ignore
    public Transaction(@NonNull final String sourceId, final int transactionType, @Nullable final String title, long amount){
        this.sourceId = sourceId;
        this.transactionType = transactionType;
        this.title = title;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    }


    @NonNull
    private String sourceId;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public void setSourceId(@NonNull String sourceId) {
        this.sourceId = sourceId;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getAmount() {
        return amount;
    }

    public int getTransactionType() {
        return transactionType;
    }

    @NonNull
    public String getSourceId() {
        return sourceId;
    }
}
