package com.whompum.PennyFlip.ActivityDashboard.Wallet.Persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

@Dao
public interface WalletDao {

    @Query("SELECT * From Wallet")
    LiveData<Wallet> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(@NonNull final Wallet wallet);

}
