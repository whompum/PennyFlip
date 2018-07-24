package com.whompum.PennyFlip.Money;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Transactions.Models.TransactionType;

import java.util.List;

@Dao
public abstract class SourceDao {

    @Insert()  //Will abort  on failure
    public abstract void insert(@NonNull final Source source);

    @Query("SELECT * FROM Source")
    public abstract LiveData<List<Source>> fetchAll(); //LiveData so it can be observed

    @Query("SELECT * FROM Source where title = :title")
    public abstract Source fetchForName(@NonNull final String title);

    @Query("SELECT * FROM Source where transactionType = :type")
    public abstract LiveData<List<Source>> fetchForType(@IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE) final int type);

    @Query("SELECT * FROM Source where creationDate = :millis")
    public abstract LiveData<List<Source>> fetchForTime(final long millis);

    @Query("DELETE FROM Source where title = :title")
    public abstract void delete(@NonNull final String title);

    @Query("UPDATE Source SET notes = :notes WHERE title = :title")
    public abstract void updateNotes(@NonNull final String title, @NonNull final String notes);

    @Query("UPDATE Source SET pennies = :pennies WHERE title = :title")
    protected abstract void updateAmount(@NonNull final String title, final long pennies);

    public void addAmount(@NonNull final String title, final long pennies){

        final Source source = fetchForName(title);

        if(source == null || pennies < 0) return;

        final long newAmt = source.getPennies() + pennies;

        updateAmount(title, newAmt);
    }

}













