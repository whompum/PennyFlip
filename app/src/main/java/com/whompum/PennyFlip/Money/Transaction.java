package com.whompum.PennyFlip.Money;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Transactions.Models.TransactionType;

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
