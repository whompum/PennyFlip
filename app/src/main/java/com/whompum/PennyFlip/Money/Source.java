package com.whompum.PennyFlip.Money;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Transactions.Models.TransactionType;

@Entity
public class Source {

    @NonNull
    @PrimaryKey
    private String title;

    @IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE)
    private int transactionType;

    @Nullable
    private String notes;

    private long pennies;

    private long creationDate;

    public Source(@NonNull String title, int transactionType, @Nullable String notes, final long pennies, final long creationDate) {
        this.title = title;
        this.transactionType = transactionType;
        this.notes = notes;
        this.pennies = pennies;
        this.creationDate = creationDate;
    }

    @Ignore //Used to create a new Barebones Source Object
    public Source(@NonNull final String title) {
        this(title, System.currentTimeMillis(), 0L);
    }

    @Ignore
    public Source(@NonNull final String title, final long creationDate, final long pennies){
        this.title = title;
        this.pennies = pennies;
        this.creationDate = creationDate;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setTransactionType(int type) {
        this.transactionType = type;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }

    public void setPennies(long pennies) {
        this.pennies = pennies;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public int getTransactionType() {
        return transactionType;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    public long getPennies() {
        return pennies;
    }

    public long getCreationDate() {
        return creationDate;
    }
}
