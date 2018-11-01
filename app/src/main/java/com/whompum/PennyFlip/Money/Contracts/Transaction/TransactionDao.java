package com.whompum.PennyFlip.Money.Contracts.Transaction;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Contracts.MoneyDaoWriter;

@Dao
public interface TransactionDao<T> extends TransactionQueryDao<T>,
        MoneyDaoId<Transaction>, MoneyDaoWriter<Transaction>{

    @Override
    @Query("SELECT * FROM `Transaction` WHERE id = :id")
    <P> Transaction fetchById(@NonNull final P id);
}
