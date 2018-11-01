package com.whompum.PennyFlip.Money.Contracts.Transaction;

import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;

public interface TransactionQueryDao<T> extends MoneyQueryBase<T> {

    @Override
    @Query("SELECT * FROM `Transaction`")
    T fetchAll();

    //TransactionType
    @Query("SELECT * From `Transaction` WHERE transactionType = :transactionType")
    T fetchByType(final int transactionType);

    //Timestamp
    @Query("SELECT * From `Transaction` WHERE timestamp >= :floor AND timestamp <= :ciel")
    T fetchByTimestamp(final long floor, final long ciel);

    //SourceId
    @Query("SELECT * FROM `Transaction` WHERE sourceId = :sourceId")
    T fetchBySource(@NonNull final String sourceId);

    //Amount
    @SuppressWarnings("unused")
    @Query("SELECT * FROM `Transaction` WHERE amount >= :floorPennies AND amount <= :cielPennies")
    T fetchByAmountRange(final long floorPennies, final long cielPennies);

    //TransactionType - Timestamp
    @Query("SELECT * FROM `Transaction` WHERE transactionType = :transactionType AND timestamp >= :floor AND timestamp <= :ciel")
    T fetchByTypeAndTime(final int transactionType, final long floor, final long ciel);

    //Timestamp - SourceId
    @Query("SELECT * From `Transaction` WHERE timestamp >= :floor AND timestamp <= :ciel AND sourceId = :sourceId")
    T fetchFromSourceAndTime(@NonNull final String sourceId, final long floor, final long ciel);


}
