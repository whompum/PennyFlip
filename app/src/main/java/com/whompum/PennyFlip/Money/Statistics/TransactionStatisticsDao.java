package com.whompum.PennyFlip.Money.Statistics;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Transaction.Transaction;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.ADD;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.SPEND;

@Dao
public abstract class TransactionStatisticsDao {

    @Query("SELECT * FROM TransactionStatistics Where transactionType = :transactionType")
    public abstract TransactionStatistics fetchByType(@IntRange(from = ADD, to = SPEND) final int transactionType);

    @Insert
    public abstract void insert(@NonNull final TransactionStatistics statistics);

    @Update
    abstract void update(TransactionStatistics newStats);

    public void onMonetaryTransaction(@NonNull final Transaction transaction){

        final TransactionStatistics stats = fetchByType( transaction.getTransactionType() );

        if( stats == null )
            insert( makeNewTransactionType( transaction ) );

        else
            updateStatistics( stats, transaction );
    }

    private TransactionStatistics makeNewTransactionType(@NonNull final Transaction t){
        return new TransactionStatistics( t.getTransactionType(), t.getAmount(), 1 );
    }

    private void updateStatistics(@NonNull final TransactionStatistics stats, @NonNull final Transaction t){
        stats.setLastTransactionId( t.getId() );
        stats.appendPennies( t.getAmount() );
        stats.incrementNumTransactions();

        update( stats );
    }

}
