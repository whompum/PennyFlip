package com.whompum.PennyFlip.Money;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.whompum.PennyFlip.Money.Contracts.Source.GenericFetchSourceById;
import com.whompum.PennyFlip.Money.Contracts.Transaction.TransactionDao;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Contracts.Source.SourceDao;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Wallet.Wallet;
import com.whompum.PennyFlip.Money.Contracts.Wallet.WalletDao;

import java.util.List;

@Database(entities = {Source.class, Transaction.class, Wallet.class}, version = 1, exportSchema = false)
public abstract class MoneyDatabase extends RoomDatabase {
    public static final String NAME = "money.db";

    public abstract SourceDao<List<Source>> getSourceAccessor();
    public abstract GenericFetchSourceById<Source> getSingleSourceAccessor();
    public abstract GenericFetchSourceById<LiveData<Source>> getObservableSingleSourceAccessor();
    public abstract SourceDao<LiveData<List<Source>>> getObservableSourceAccessor();

    public abstract TransactionDao<List<Transaction>> getTransactionAccessor();
    public abstract TransactionDao<LiveData<List<Transaction>>> getObservableTransactionAccessor();

    public abstract WalletDao getWalletAccessor();
}
