package com.whompum.PennyFlip.Money.Contracts.Wallet;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.Wallet.Wallet;

@Dao
public abstract class WalletDao implements MoneyDaoId<Wallet> {

    @Override
    public <I> Wallet fetchById(@NonNull final I id){
        return fetchWallet();
    }

    @Query("SELECT * FROM Wallet")
    public abstract Wallet fetchWallet();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void update(@NonNull final Wallet wallet);

}
