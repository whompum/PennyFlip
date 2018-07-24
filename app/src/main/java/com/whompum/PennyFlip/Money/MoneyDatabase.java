package com.whompum.PennyFlip.Money;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Source.class, Transaction.class}, version = 1, exportSchema = false)
public abstract class MoneyDatabase extends RoomDatabase {

    public abstract SourceDao getSourceAccessor();
    public abstract SourceDao getTransactionAccessor();

}
