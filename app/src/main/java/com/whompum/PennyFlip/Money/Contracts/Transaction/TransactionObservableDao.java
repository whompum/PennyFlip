package com.whompum.PennyFlip.Money.Contracts.Transaction;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

import java.util.List;

@Dao
public interface TransactionObservableDao {

    @Query("SELECT * FROM `Transaction`")
    LiveData<List<Transaction>> fetchAll();

    @Query("SELECT * FROM `Transaction` WHERE sourceId = :sourceId")
    LiveData<List<Transaction>> fetchBySource(@NonNull final String sourceId);

    @Query("SELECT * FROM `Transaction` WHERE timestamp >= :mFloor AND timestamp <= :mCiel")
    LiveData<List<Transaction>> fetchByTime(final long mFloor, final long mCiel);

    @Query("SELECT * FROM `Transaction` WHERE transactionType = :type")
    LiveData<List<Transaction>> fetchByType(@IntRange(from = TransactionType.INCOME,
            to = TransactionType.CALLIBRATE) final int type);

    @Query("SELECT * FROM `Transaction` WHERE transactionType = :type AND timestamp >= :mFloor AND timestamp <= :mCiel")
    LiveData<List<Transaction>> fetchByTypeAndTime(@IntRange(from = TransactionType.INCOME,
            to = TransactionType.CALLIBRATE) final int type, final long mFloor, final long mCiel);
}











