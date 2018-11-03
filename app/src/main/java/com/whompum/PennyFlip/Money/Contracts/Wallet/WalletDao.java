package com.whompum.PennyFlip.Money.Contracts.Wallet;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Wallet.Wallet;

@Dao
public interface WalletDao {


    @Query("SELECT * FROM Wallet")
    Wallet fetchWallet();

    @Query("SELECT * FROM WALLET")
    LiveData<Wallet> fetchObservableWallet();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void update(@NonNull final Wallet wallet);

}
