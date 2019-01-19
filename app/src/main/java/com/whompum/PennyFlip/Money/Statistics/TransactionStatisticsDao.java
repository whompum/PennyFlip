package com.whompum.PennyFlip.Money.Statistics;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.List;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.ADD;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.SPEND;

@Dao
public abstract class TransactionStatisticsDao {

    @Query("SELECT * FROM TransactionStatistics Where transactionType = :transactionType")
    public abstract TransactionStatistics fetchByType(@IntRange(from = ADD, to = SPEND) final int transactionType);

    @Query("SELECT * FROM TransactionStatistics")
    public abstract List<TransactionStatistics> fetchAll();

    @Query("SELECT netAmount FROM TransactionStatistics WHERE transactionType = :type")
    public abstract long fetchNetAmount(@IntRange(from = ADD, to = SPEND) final int type);

    @Insert
    public abstract void insert(@NonNull final TransactionStatistics statistics);

    @Update
    abstract void update(TransactionStatistics newStats);

    @WorkerThread
    public void onMonetaryTransaction(@NonNull final Transaction t){

        //First check if the statistics item exists.
        //If it does, update it
        //Else initialize one, and insert

        TransactionStatistics stats = fetchByType( t.getTransactionType() );

        if( stats == null ) {
            stats = new TransactionStatistics( t.getTransactionType() );
            updateStatistics( stats, t );
            insert( stats );
        }
        else {
            updateStatistics( stats, t );
            update( stats );
        }

        final TransactionStatistics oppositeStats =
                fetchByType( (t.getTransactionType() == ADD) ? SPEND : ADD );

        if( oppositeStats != null ){
            updateTransactionRatio( oppositeStats );
            update( oppositeStats );
        }

    }


    private void updateStatistics(@NonNull final TransactionStatistics s,
                                  @NonNull final Transaction t){

        s.updateFromTransaction( t );
        updateTransactionRatio( s );
    }

    private void updateTransactionRatio(@NonNull final TransactionStatistics s){

        final long netAmount = s.getNetAmount();

        final long oppositeNetAmount = fetchNetAmount( (s.getTransactionType() == ADD) ? SPEND : ADD );

        if( netAmount <= 0L )
            s.setTransactionRatio( 0D );

        else if( oppositeNetAmount <= 0L ) // Has a net amount while the other doesn't
            s.setTransactionRatio( 100D );

        else // Both have a net amount
            s.setTransactionRatio( (double)s.getNetAmount() / (double)oppositeNetAmount );

    }

}
