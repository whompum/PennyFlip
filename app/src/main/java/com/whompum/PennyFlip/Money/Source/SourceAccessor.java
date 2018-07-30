package com.whompum.PennyFlip.Money.Source;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Transaction.Transaction;

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

    @Query("UPDATE Source SET lastUpdate = :lastUpdate where TITLE = :title")
    protected abstract void updateTimestamp(@NonNull final String title, final long lastUpdate);

    public void addAmount(@NonNull final Transaction transaction){

        final String id = transaction.getSourceId();
        final long amt = transaction.getAmount();
        final long timestamp = transaction.getTimestamp();

        final Source source = fetch(id);

        if(source == null || amt < 0) return;

        final long newAmt = source.getPennies() + amt;

        updateAmount(id, newAmt);
        updateTimestamp(id, timestamp);
    }

}
