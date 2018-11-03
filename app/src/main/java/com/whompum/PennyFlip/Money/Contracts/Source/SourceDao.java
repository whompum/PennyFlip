package com.whompum.PennyFlip.Money.Contracts.Source;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoWriter;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

import java.util.List;

@Dao
public abstract class SourceDao implements MoneyDaoWriter<Source>{

    @Query("SELECT * FROM Source")
    public abstract List<Source> fetchAll();

    @Query("SELECT * FROM Source WHERE title = :title")
    public abstract Source fetchById(@NonNull final String title);

    //Fetches a list of Sources whose title is 'like' `title`
    @Query("SELECT * FROM Source WHERE title like :title")
    public abstract List<Source> fetchLikeTitle(@NonNull final String title);

    //Fetches a list of source whose title is 'like' `title` and whose type is of `type`
    @Query("SELECT * FROM Source WHERE title like :title AND transactionType = :type")
    public abstract List<Source> fetchLikeTitleOfType(@NonNull final String title,
                           @IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE) final int type);

    @Query("SELECT * FROM Source WHERE creationDate >= :mFloor AND creationDate <= :mCiel")
    public abstract List<Source> fetchTimeRange(final long mFloor, final long mCiel);

    //Fetches a list of Sources whose type is of `type`
    @Query("SELECT * FROM Source where transactionType = :type")
    public abstract List<Source> fetchOfType(@IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE) final int type);


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
    public void addAmount(@NonNull final Transaction transaction){

        final String id = transaction.getSourceId();
        final long amt = transaction.getAmount();
        final long timestamp = transaction.getTimestamp();

        if( amt <= 0 ) return;

        final Source src = fetchById(transaction.getSourceId());

        final long newAmt = src.getPennies() + amt;

        updateAmount(id, newAmt);
        updateLastUpdateTimestamp(id, timestamp);
    }

}






















