package com.whompum.PennyFlip.Money;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

@Dao
public interface WalletDao {

    @Query("SELECT * From Wallet")
    LiveData<Wallet> fetchObservable();

    @Query("SELECT * FROM Wallet")
    Wallet fetch();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(@NonNull final Wallet wallet);

}
