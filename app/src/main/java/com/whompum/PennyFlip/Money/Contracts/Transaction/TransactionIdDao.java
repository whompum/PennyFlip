package com.whompum.PennyFlip.Money.Contracts.Transaction;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

@Dao
public abstract class TransactionIdDao implements MoneyDaoId<Transaction> {

    @Override
    public <P> Transaction fetchById(@NonNull final P id){
        if( !(id instanceof Integer) )
            return null;

        return fetchById(id);
    }

    @Query("SELECT * FROM `Transaction` WHERE id = :id")
    public abstract Transaction fetchById(@NonNull final Integer id);

}
