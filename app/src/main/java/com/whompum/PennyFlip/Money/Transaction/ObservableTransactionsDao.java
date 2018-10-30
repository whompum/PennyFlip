package com.whompum.PennyFlip.Money.Transaction;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Queries.MoneyDao;

import java.util.List;

@Dao
public interface ObservableTransactionsDao extends MoneyDao<LiveData<List<Transaction>>, Integer> {

    @Query("SELECT * FROM `Transaction` WHERE sourceId = :sourceId")
    LiveData<List<Transaction>> observeBySource(@NonNull final String sourceId);

    @Query("SELECT * FROM `Transaction` WHERE timestamp >= :mFloor AND timestamp <= :mCiel")
    LiveData<List<Transaction>> observeByTimerange(final long mFloor, final long mCiel);

}
