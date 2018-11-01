package com.whompum.PennyFlip.Money.Contracts.Source;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoWriter;
import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

@Dao
public abstract class SourceDao<T> implements MoneyQueryBase<T>, MoneyDaoWriter<Source>{


    //Sets the amount of the source @ `title` by `pennies`
    @Query("UPDATE Source SET pennies = :pennies WHERE title = :title")
    protected abstract void updateAmount(@NonNull final String title, final long pennies);

    //Sets the timestamp @ source `title` to `lastupdate`
    @Query("UPDATE Source SET lastUpdate = :lastUpdate where TITLE = :title")
    protected abstract void updateLastUpdateTimestamp(@NonNull final String title, final long lastUpdate);

    //Deletes a source. MAY BECOME AN ANTIQUATED FEATURE
    @Query("DELETE FROM Source where title = :title")
    public abstract void delete(@NonNull final String title);

    /**
     * Aggregated operation that updates the source object the
     * {@link Transaction} was made under.
     *
     * Data fields updated are the amount, and the timestamp.
     *
     * @param transaction The newly created transaction object to update the source by
     */
    @WorkerThread
    public void addAmount(@NonNull final Transaction transaction, @NonNull final Source source){

        final String id = transaction.getSourceId();
        final long amt = transaction.getAmount();
        final long timestamp = transaction.getTimestamp();

        if( amt <= 0 ) return;

        final long newAmt = source.getPennies() + amt;

        updateAmount(id, newAmt);
        updateLastUpdateTimestamp(id, timestamp);
    }

}






















