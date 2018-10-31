package com.whompum.PennyFlip.Money.Contracts.Transaction;

import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.List;

public interface TransactionQueryContract extends
        MoneyDaoId<Transaction>, com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase<List<Transaction>> {

    @Override
    @Query("SELECT * FROM `Transaction`")
    List<Transaction> fetchAll();

    //TransactionType
    @Query("SELECT * From `Transaction` WHERE transactionType = :transactionType")
    List<Transaction> fetchByType(final int transactionType);

    //Timestamp
    @Query("SELECT * From `Transaction` WHERE timestamp >= :floor AND timestamp <= :ciel")
    List<Transaction> fetchByTimestamp(final long floor, final long ciel);

    //SourceId
    @Query("SELECT * FROM `Transaction` WHERE sourceId = :sourceId")
    List<Transaction> fetchBySource(@NonNull final String sourceId);

    //Amount
    @SuppressWarnings("unused")
    @Query("SELECT * FROM `Transaction` WHERE amount >= :floorPennies AND amount <= :cielPennies")
    List<Transaction> fetchByAmountRange(final long floorPennies, final long cielPennies);

    //TransactionType - Timestamp
    @Query("SELECT * FROM `Transaction` WHERE transactionType = :transactionType AND timestamp >= :floor AND timestamp <= :ciel")
    List<Transaction> fetchByTypeAndTime(final int transactionType, final long floor, final long ciel);

    //Timestamp - SourceId
    @Query("SELECT * From `Transaction` WHERE timestamp >= :floor AND timestamp <= :ciel AND sourceId = :sourceId")
    List<Transaction> fetchFromSourceAndTime(@NonNull final String sourceId, final long floor, final long ciel);

}
