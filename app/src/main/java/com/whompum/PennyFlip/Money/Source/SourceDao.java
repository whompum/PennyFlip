package com.whompum.PennyFlip.Money.Source;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.whompum.PennyFlip.Money.Writes.MoneyDaoWriter;
import com.whompum.PennyFlip.Money.Queries.MoneyDao;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

import java.util.List;

@Dao
public abstract class SourceDao /*Perhaps make generic types for LiveData extension*/
        implements MoneyDao<Source, String>, MoneyDao.MoneyDaoId, MoneyDaoWriter<Source> {

    //Fetch every source in the database
    @Override
    @Query("SELECT * FROM source")
    public abstract List<Source> fetchAll();

    //Fetch a single source by its title
    @Override
    @Query("SELECT * FROM Source WHERE title = :title")
    public abstract Source fetchById(@NonNull String title);

    //Insert a new source
    @Override
    @Insert //Aborts on failure if a primary key constraint is broken (title)
    public abstract void insert(@NonNull Source o);

    //Fetches a list of Sources whose title is 'like' `title`
    @Query("SELECT * FROM Source WHERE title like :title")
    public abstract List<Source> fetchNonExact(@NonNull final String title);

    //Fetches a list of source whose title is 'like' `title` and whose type is of `type`
    @Query("SELECT * FROM Source WHERE title like :title AND transactionType = :type")
    public abstract List<Source> fetchNonExactOfType(@NonNull final String title,
                                                     @IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE) final int type);

    //Fetches a list of Sources whose type is of `type`
    @Query("SELECT * FROM Source where transactionType = :type")
    public abstract List<Source> fetch(@IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE) final int type);

    //Deletes a source. MAY BECOME AN ANTIQUATED FEATURE
    @Query("DELETE FROM Source where title = :title")
    public abstract void delete(@NonNull final String title);

    //Sets the amount of the source @ `title` by `pennies`
    @Query("UPDATE Source SET pennies = :pennies WHERE title = :title")
    protected abstract void updateAmount(@NonNull final String title, final long pennies);

    //Sets the timestamp @ source `title` to `lastupdate`
    @Query("UPDATE Source SET lastUpdate = :lastUpdate where TITLE = :title")
    protected abstract void updateLastUpdateTimestamp(@NonNull final String title, final long lastUpdate);

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

        final Source source = fetchById(id);

        if(source == null || amt <= 0) return;

        final long newAmt = source.getPennies() + amt;

        updateAmount(id, newAmt);
        updateLastUpdateTimestamp(id, timestamp);
    }

}






















