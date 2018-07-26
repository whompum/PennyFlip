package com.whompum.PennyFlip.Money.Transaction;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import java.util.List;

@Dao
public interface  TransactionDao {

    //ALL
    @Query("SELECT * FROM `Transaction`")
    LiveData<List<Transaction>> fetch();

    //ID
    @Query("SELECT * FROM `Transaction` WHERE sourceId = :sourceId")
    List<Transaction> fetch(@NonNull final String sourceId);

    //TransactionType
    @Query("SELECT * From `Transaction` WHERE transactionType = :transactionType")
    LiveData<List<Transaction>> fetch(final int transactionType);

    //Timestamp
    @Query("SELECT * From `Transaction` WHERE timestamp <= :floor AND timestamp >= :ciel")
    List<Transaction> fetch(final long floor, final long ciel);

    //TransactionType - Timestamp
    @Query("SELECT * FROM `Transaction` WHERE transactionType = :transactionType AND timestamp <= :floor AND timestamp >= :ciel")
    List<Transaction> fetch(final int transactionType, final long floor, final long ciel);

    //Timestamp - ID
    @Query("SELECT * From `Transaction` WHERE timestamp <= :floor AND timestamp >= :ciel AND sourceId = :sourceId")
    List<Transaction> fetch(@NonNull final String sourceId, final long floor, final long ciel);


    @Query("DELETE FROM `Transaction`  WHERE sourceId = :sourceId")
    void deleteForSource(@NonNull final String sourceId); // May not need to be used.

    @Insert//aborts
    void insert(@NonNull final Transaction transaction);

}
