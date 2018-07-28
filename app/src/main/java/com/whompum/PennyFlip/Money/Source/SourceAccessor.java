package com.whompum.PennyFlip.Money.Source;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Transactions.Models.TransactionType;

import java.util.List;

public abstract class SourceAccessor{

    @Query("SELECT * FROM Source where title = :title") //By Title
    public abstract Source fetch(@NonNull final String title);

    //@Query("SELECT * FROM Source where creationDate = :millis")
    //public abstract LiveData<List<Source>> fetchForTime(final long millis);

    @Insert()  //Will abort  on failure
    public abstract void insert(@NonNull final Source source);

    @Query("DELETE FROM Source where title = :title")
    public abstract void delete(@NonNull final String title);

    @Query("UPDATE Source SET notes = :notes WHERE title = :title")
    public abstract void updateNotes(@NonNull final String title, @NonNull final String notes);

    @Query("UPDATE Source SET pennies = :pennies WHERE title = :title")
    protected abstract void updateAmount(@NonNull final String title, final long pennies);

    public void addAmount(@NonNull final String title, final long pennies){

        final Source source = fetch(title);

        if(source == null || pennies < 0) return;

        final long newAmt = source.getPennies() + pennies;

        updateAmount(title, newAmt);
    }

}
