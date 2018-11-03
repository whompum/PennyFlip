package com.whompum.PennyFlip.Money;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.whompum.PennyFlip.Money.Contracts.Source.SourceObservableDao;
import com.whompum.PennyFlip.Money.Contracts.Transaction.TransactionDao;
import com.whompum.PennyFlip.Money.Contracts.Transaction.TransactionObservableDao;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Contracts.Source.SourceDao;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Wallet.Wallet;
import com.whompum.PennyFlip.Money.Contracts.Wallet.WalletDao;


@Database(entities = {Source.class, Transaction.class, Wallet.class}, version = 1, exportSchema = false)
public abstract class MoneyDatabase extends RoomDatabase {
    public static final String NAME = "money.db";

    public abstract SourceDao getSourceAccessor();
    public abstract SourceObservableDao getObservableSourceAccessor();
    public abstract TransactionDao getTransactionAccessor();
    public abstract TransactionObservableDao getObservableTransactionAccessor();
    public abstract WalletDao getWalletAccessor();
}
