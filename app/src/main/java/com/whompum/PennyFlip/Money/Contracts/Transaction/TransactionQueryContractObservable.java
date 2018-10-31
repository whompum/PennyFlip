package com.whompum.PennyFlip.Money.Contracts.Transaction;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.List;

@Dao
public abstract class TransactionQueryContractObservable implements
        MoneyQueryBase<LiveData<List<Transaction>>>, MoneyDaoId<LiveData<Transaction>> {

    @Query("SELECT * FROM `Transaction` WHERE sourceId = :sourceId")
    public abstract LiveData<List<Transaction>> observeBySource(@NonNull final String sourceId);

    @Query("SELECT * FROM `Transaction` WHERE timestamp >= :mFloor AND timestamp <= :mCiel")
    public abstract LiveData<List<Transaction>> observeByTimerange(final long mFloor, final long mCiel);

}
