package com.whompum.PennyFlip.ActivityDashboard.Wallet.Persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Wallet.class}, version = 1, exportSchema = false)
public abstract class WalletRoom extends RoomDatabase {
    public abstract WalletDao getDao();
}
