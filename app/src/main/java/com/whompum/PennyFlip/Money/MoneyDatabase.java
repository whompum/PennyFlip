package com.whompum.PennyFlip.Money;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.SourceDao;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionDao;

@Database(entities = {Source.class, Transaction.class, Wallet.class}, version = 1, exportSchema = false)
public abstract class MoneyDatabase extends RoomDatabase {

    public static final String NAME = "money.db";

    public abstract SourceDao getSourceAccessor();
    public abstract TransactionDao getTransactionAccessor();
    public abstract WalletDao getWalletAccessor();
}
