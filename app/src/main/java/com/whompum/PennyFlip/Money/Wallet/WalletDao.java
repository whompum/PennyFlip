package com.whompum.PennyFlip.Money.Wallet;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Queries.MoneyDao;

import java.util.List;

@Dao
public interface WalletDao extends MoneyDao<Wallet, Integer> {

    @Override
    @Query("SELECT * FROM Wallet")
    List<Wallet> fetchAll();

    @Override
    @Query("SELECT * FROM Wallet WHERE id = :id")
    Wallet fetchById(@NonNull final Integer id);

    @Query("SELECT * From Wallet")
    LiveData<Wallet> fetchObservable();

    @Deprecated
    @Query("SELECT * FROM Wallet")
    Wallet fetch();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(@NonNull final Wallet wallet);

}
