package com.whompum.PennyFlip.Money.Transaction;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Writes.MoneyDaoWriter;

@Dao
public interface TransactionDAO extends TransactionDaoQueries, MoneyDaoWriter<Transaction>{


    @Override
    @Insert
    void insert(@NonNull final Transaction o);

    /*
        TODO Add from `amount` on
     */

    /*
        all: All
        id: Singular

        transactionType: All
        timestamp: All
        sourceId: All
        amount: All

        transactionType - Timestamp: All
        transactionType - SourceId: All
        Timestamp - SourceId: All

        transactionType - Timestamp - SourceId: All

        amount - transactionType: All
        amount - sourceId: All
        amount - Timestamp: All

        amount - transactionType - sourceId: All
        amount - transactionType - timestamp: All
        amount - timestamp - sourceId: All

        amount - transactionType - sourceId - Timestamp: All

     */

}
