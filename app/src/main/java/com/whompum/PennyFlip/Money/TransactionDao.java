package com.whompum.PennyFlip.Money;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import java.util.List;

@Dao
public interface TransactionDao {

    @Query("SELECT * FROM `Transaction`")
    List<Transaction> fetchAll();

    @Query("SELECT * FROM `Transaction` WHERE sourceId = :sourceId")
    List<Transaction> fetchForSource(@NonNull final String sourceId);

    @Query("SELECT * From `Transaction` WHERE transactionType = :transactionType")
    List<Transaction> fetchForTransactionType(final int transactionType);

    @Query("SELECT * From `Transaction` WHERE timestamp LIKE :timestamp")
    List<Transaction> fetchForTimestamp(final long timestamp);

    @Query("SELECT * From `Transaction` WHERE timestamp LIKE :timestamp AND sourceId = :sourceId")
    List<Transaction> fetchForTimestamp(@NonNull final String sourceId, final long timestamp);

    @Query("DELETE FROM `Transaction`  WHERE sourceId = :sourceId")
    void deleteForSource(@NonNull final String sourceId); // May not need to be used.

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(@NonNull final Transaction transaction);


}
